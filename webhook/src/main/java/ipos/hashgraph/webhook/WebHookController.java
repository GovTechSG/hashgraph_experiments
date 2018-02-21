package ipos.hashgraph.webhook;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebHookController {

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    void listenToTransactions(@RequestBody String block) {
        System.out.println("What is in the block: "+block);
    }

}
