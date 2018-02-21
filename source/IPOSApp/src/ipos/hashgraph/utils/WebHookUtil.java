package ipos.hashgraph.utils;

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

    public static void postToWebHook(Block block) {
        String url = "http://localhost:8080/webhook";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpput = new HttpPut(url);
        httpput.addHeader("Content-type", "application/json");
        StringEntity params = null;
        try {
            Marshaller marshaller = new JacksonMarshaller();
            params = new StringEntity(marshaller.marshalLenient(block));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); //TODO
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
