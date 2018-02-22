package ipos.hashgraph.transactions;

import com.txmq.exo.messaging.ExoMessage;
import com.txmq.exo.transactionrouter.ExoTransaction;
import ipos.hashgraph.IPOSState;
import ipos.hashgraph.model.Document;

public class IPOSTransactions {

	@ExoTransaction(TransactionTypes.ADD_DOC)
	public void addDocument(ExoMessage message, IPOSState state) {
		Document document = (Document) message.payload;
		state.addDocument(document.getHash());
	}
}
