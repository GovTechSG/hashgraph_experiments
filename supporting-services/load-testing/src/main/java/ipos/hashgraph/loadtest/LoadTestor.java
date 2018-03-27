package ipos.hashgraph.loadtest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoadTestor {

    @Value("${service.baseurl}")
    private String[] baseUrl;

    //publish transaction
    //time taken to confirm the transaction

    public void publishtransaction() {

    }



}
