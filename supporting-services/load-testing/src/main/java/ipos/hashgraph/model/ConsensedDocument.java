package ipos.hashgraph.model;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class ConsensedDocument {

    String transactionType;
    Document payload;
    Instant consensusTimestamp;
    UUID id;

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Document getPayload() {
        return payload;
    }

    public void setPayload(Document payload) {
        this.payload = payload;
    }

    public Instant getConsensusTimestamp() {
        return consensusTimestamp;
    }

    public void setConsensusTimestamp(Instant consensusTimestamp) {
        this.consensusTimestamp = consensusTimestamp;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
