package ipos.hashgraph.utils;

import com.txmq.exo.messaging.ExoMessage;
import com.txmq.exo.persistence.Block;
import ipos.hashgraph.utils.marshaller.JacksonMarshaller;
import ipos.hashgraph.utils.marshaller.Marshaller;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class WebHookUtil {

    public static void postToWebHook(ExoMessage message) {
        String url = "http://localhost:8081/webhook";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpput = new HttpPut(url);
        httpput.addHeader("Content-type", "application/json");
        StringEntity params = null;
        try {
            Marshaller marshaller = new JacksonMarshaller();
            params = new StringEntity(marshaller.marshalLenient(MessageTransformer.getConsensedDocument(message)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        params.setContentType("application/json");
        httpput.setEntity(params);

        try {
            client.execute(httpput);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
