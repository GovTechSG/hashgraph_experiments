package ipos.hashgraph.transaction;

import com.txmq.exo.messaging.ExoMessage;
import com.txmq.exo.messaging.ExoTransactionType;
import com.txmq.exo.transactionrouter.ExoTransaction;
import com.txmq.exo.core.ExoState;


public class EndpointsTransaction {
	@ExoTransaction(ExoTransactionType.ANNOUNCE_NODE)
	public void announceNode(ExoMessage message, ExoState state) {
		state.addEndpoint((String) message.payload);
	}
}
