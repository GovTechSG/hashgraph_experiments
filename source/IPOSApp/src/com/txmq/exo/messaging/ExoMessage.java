package com.txmq.exo.messaging;

import java.io.*;
import java.time.Instant;
import java.util.UUID;

/**
 * ExoMessage is the base wrapper for transaction that come in through Exo.
 * The framework is designed to work with instances of ExoMessage.  It is
 * unknown if it will work with subclasses of ExoMessage (likely not),
 * but it should be flexible enough for most cases.
 * 
 * It takes an ExoTrasnactionType ((which can and should be subclassed)) and
 * a payload, which can be anything that implements Serializable.  Your payload
 * can be designed in any application-specific way, Exo doesn't care.
 * 
 * @see
 */
public class ExoMessage implements Serializable {

	public ExoTransactionType transactionType;

	public Serializable payload;

	public Instant consensusTimestamp;

	public int uuidHash;

	
	public ExoMessage() {
		super();
		this.transactionType = new ExoTransactionType();
		this.uuidHash = UUID.randomUUID().hashCode();
	}
	
	public ExoMessage(ExoTransactionType transactionType) {
		super();
		this.transactionType = transactionType;	
		this.uuidHash = UUID.randomUUID().hashCode();
	}
	
	public ExoMessage(ExoTransactionType transactionType, Serializable payload) {
		super();
		this.transactionType = transactionType;				
		this.payload = payload;
		this.uuidHash = UUID.randomUUID().hashCode();
	}

	public ExoMessage(int uuid, ExoTransactionType transactionType, Serializable payload, Instant consensusTimestamp) {
		super();
		this.transactionType = transactionType;
		this.payload = payload;
		this.uuidHash = uuid;
		this.consensusTimestamp = consensusTimestamp;
	}
	
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(this);
		o.close();
		return b.toByteArray();
	}

	public static ExoMessage deserialize(byte[] b) throws IOException, ClassNotFoundException {
		ObjectInputStream o = new ObjectInputStream(new ByteArrayInputStream(b));
		ExoMessage result = (ExoMessage) o.readObject();
		o.close();
		
		return result;
	}

    public ExoTransactionType getTransactionType() {
        return transactionType;
    }

    public Serializable getPayload() {
        return payload;
    }

    public Instant getConsensusTimestamp() {
        return consensusTimestamp;
    }

    public int getUuidHash() {
        return uuidHash;
    }
}
