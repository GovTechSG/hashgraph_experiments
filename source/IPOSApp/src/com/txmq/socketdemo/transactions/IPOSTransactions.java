package com.txmq.socketdemo.transactions;

import com.txmq.exo.messaging.ExoMessage;
import com.txmq.exo.transactionrouter.ExoTransaction;
import com.txmq.socketdemo.IPOSState;
import com.txmq.socketdemo.SocketDemoTransactionTypes;
import ipos.hashgraph.model.Document;

public class IPOSTransactions {

	@ExoTransaction(SocketDemoTransactionTypes.ADD_DOC)
	public void addDocument(ExoMessage message, IPOSState state) {
		Document document = (Document) message.payload;
		state.addDocument(document.getHash());
	}
}
