package ipos.hashgraph.transaction;

import com.txmq.exo.messaging.ExoTransactionType;

public class TransactionType extends ExoTransactionType {
	public static final String GET_DOCS = "GET_DOCS";
	public static final String ADD_DOC = "ADD_DOC";
	
	private static final String[] values = {
			GET_DOCS,
			ADD_DOC
	};
	
	public TransactionType() {
		super();
		if (getInitialized() == false) {
			initialize(values);
		}
	}
	
	public TransactionType(String transactionType) {
		super();
		if (getInitialized() == false) {
			initialize(values);
		}
		
		this.setValue(transactionType);
	}
}
