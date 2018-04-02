package ipos.hashgraph.model;

import ipos.hashgraph.transaction.TransactionType;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class ConsensedDocument {

    String transactionType;
    Serializable payload;
    String consensusTimestamp;
    UUID id;

    public Serializable getPayload() {
        return payload;
    }

    public void setPayload(Serializable payload) {
        this.payload = payload;
    }

    public String getConsensusTimestamp() {
        return consensusTimestamp;
    }

    public void setConsensusTimestamp(String consensusTimestamp) {
        this.consensusTimestamp = consensusTimestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
