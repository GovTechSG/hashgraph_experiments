package com.txmq.exo.core;

import com.swirlds.platform.Address;
import com.swirlds.platform.AddressBook;
import com.swirlds.platform.Platform;
import com.swirlds.platform.SwirldState;
import com.txmq.exo.messaging.ExoMessage;
import ipos.hashgraph.IPOSState;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ExoState is a base class for developers to extend when implementing Swirlds states.
 * ExoState encapsulates persisting the address book, enables the collection of data
 * about available endpoints (exposed by the endpoints API), and contains the hooks
 * for routing transactions using annotations.
 * 
 * Developers should be sure to call super-methods when extending/implementing init, 
 * copyFrom, and handleTransaction when subclassing ExoState.
 * 
 * @see
 */
public class ExoState {
	
	protected AddressBook addressBook;
	
	protected String myName;
	
	private List<String> endpoints = Collections.synchronizedList(new ArrayList<String>());

	public synchronized List<String> getEndpoints() {
		return endpoints;
	}
	
	public synchronized void addEndpoint(String endpoint) {
		this.endpoints.add(endpoint);
	}
	
	public synchronized void copyFrom(SwirldState old) {
		endpoints = Collections.synchronizedList(new ArrayList<String>(((ExoState) old).endpoints));
		addressBook = ((IPOSState) old).addressBook.copy();
		myName = ((IPOSState) old).myName;
	}
	
	public synchronized void handleTransaction(long id, boolean consensus,
			Instant timeCreated, byte[] transaction, Address address) {
		
		try {
            ExoMessage message = ExoMessage.deserialize(transaction);
			if (consensus) {
                ExoMessage confirmedTransaction = new ExoMessage(message.getUuidHash(), message.getTransactionType(), message.getPayload(), timeCreated);
				ExoPlatformLocator.getBlockLogger().addTransaction(confirmedTransaction, this.myName);
			}
			ExoPlatformLocator.getTransactionRouter().routeTransaction(message, this);				
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
	}

	public synchronized void init(Platform platform, AddressBook addressBook) {
		this.myName = platform.getAddress().getSelfName();
		this.addressBook = addressBook;
	}

}
