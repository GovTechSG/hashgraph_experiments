package ipos.hashgraph.transaction;

import com.txmq.exo.messaging.ExoMessage;
import com.txmq.exo.transactionrouter.ExoTransaction;
import ipos.hashgraph.IPOSAppState;
import ipos.hashgraph.model.Document;

public class IPOSTransaction {

	@ExoTransaction(TransactionType.ADD_DOC)
	public void addDocument(ExoMessage message, IPOSAppState state) {
		Document document = (Document) message.payload;
		state.addDocument(document.getHash());
	}
}
