package ipos.hashgraph.loadtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import ipos.hashgraph.loadtest.model.Response;
import ipos.hashgraph.model.ConsensedDocument;
import ipos.hashgraph.model.Document;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Component
public class LoadTestor {

    public static final String NODE_1 = "http://127.0.0.1:52204/Hashgraph/1.0.0/document";
    public static final String NODE_2 = "http://127.0.0.1:52205/Hashgraph/1.0.0/document";
    public static final String NODE_3 = "http://127.0.0.1:52206/Hashgraph/1.0.0/document";

    int transactionpublished =  0;
    static int count = 0;
    static Map<String, Instant> result = new HashMap<>();
    static List<String> timeDifferenceList = new ArrayList<>();

    public void publishTransaction(String url, Document document) {
        ClientHttpRequestFactory requestFactory = getClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Document> request = new HttpEntity<>(document, httpHeaders);
        Response response = restTemplate.postForObject(url, request, Response.class);

        if(response.isResult() == true) {
            System.out.println("success");
            transactionpublished++;
        }

        System.out.println("transactionpublished:"+transactionpublished);
    }

    public static void getTransaction() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity1 = restTemplate.getForEntity("http://127.0.0.1:52204/Hashgraph/1.0.0/documents", String.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new Jdk8Module());
        mapper.registerModule(new JavaTimeModule());
        ConsensedDocument[] responseEntity = null;
        try {
            responseEntity = mapper.readValue(responseEntity1.getBody(), ConsensedDocument[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ConsensedDocument[] documents = responseEntity;
        for(int i=0; i < documents.length; i++) {
            ConsensedDocument consensedDocument = documents[i];
            Document consensedPayload = consensedDocument.getPayload();
            Instant publishedTime = result.get(consensedPayload.getHash());
            timeDifferenceList.add(timeDifference(publishedTime, consensedDocument.getConsensusTimestamp())+"");
        }
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        int timeout = 120000; //2 mints
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        return clientHttpRequestFactory;
    }


    public static void main(String[] args) {
        LoadTestor testor = new LoadTestor();
        List<String> urls = new ArrayList<>();
        urls.add(NODE_1);
        urls.add(NODE_2);
        urls.add(NODE_3);

        urls.parallelStream().forEach(u -> {
            IntStream.range(0, 1000).forEach(i -> {
                count++;
                Document document = data();
                testor.publishTransaction(u, document);
                result.put(document.getHash(), Instant.now());
            });
        });

        getTransaction();
        toWriteToFile();

    }

    //check the time difference
    public static long timeDifference (Instant publishedTime, Instant consensedTime) {
        if(publishedTime == null) return 0L;
        Duration d =
                Duration.between(
                        publishedTime ,
                        consensedTime
                );

        return (d.toMillis()/1000);

    }



    private static Document data () {
        Document document = new Document();
        document.setUserId("userId:"+count);
        document.setHash("c4d27241fcddc60ebebd8b81a509a28a91d30a1ea2a9883bcc4009db17c54e6f:"+count);
        Map<String, String> meta = new HashMap<>();
        meta.put("data1", "What is Lorem Ipsum?\n" +
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                "\n" +
                "Why do we use it?\n" +
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout.");

        document.setMeta(meta);
        byte[] data = SerializationUtils.serialize(document);
        System.out.println(data.length);
        return document;

    }

    private static void toWriteToFile()  {
        Path path = Paths.get("output.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            timeDifferenceList.stream().forEach(d -> {
                try {
                    writer.append(d+"\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
