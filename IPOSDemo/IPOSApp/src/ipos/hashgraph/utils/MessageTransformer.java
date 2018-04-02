package ipos.hashgraph.utils;

import com.txmq.exo.messaging.ExoMessage;
import ipos.hashgraph.model.ConsensedDocument;

public class MessageTransformer {

    public static ConsensedDocument getConsensedDocument(ExoMessage exoMessage) {
        ConsensedDocument consensedDocument = new ConsensedDocument();
        consensedDocument.setId(exoMessage.getUuidHash());
        consensedDocument.setConsensusTimestamp(exoMessage.getConsensusTimestamp().toString());
        consensedDocument.setPayload(exoMessage.getPayload());
        consensedDocument.setTransactionType(exoMessage.getTransactionType().getValue());
        return consensedDocument;
    }

}
