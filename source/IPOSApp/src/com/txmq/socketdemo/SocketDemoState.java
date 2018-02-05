package com.txmq.socketdemo;

import com.swirlds.platform.*;
import com.txmq.exo.core.ExoState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * This holds the current state of the swirld. For this simple "hello swirld" code, each transaction is just
 * a string, and the state is just a list of the strings in all the transactions handled so far, in the
 * order that they were handled.
 */
public class SocketDemoState extends ExoState implements SwirldState {
	
	private List<String> documents = Collections
			.synchronizedList(new ArrayList<String>());

	public synchronized void addDocuments(String name) {
		this.documents.add(name);
	}
	

	@Override
	public synchronized AddressBook getAddressBookCopy() {
		return addressBook.copy();
	}

	@Override
	public synchronized FastCopyable copy() {
		SocketDemoState copy = new SocketDemoState();
		copy.copyFrom(this);
		return copy;
	}

	@Override
	public void copyTo(FCDataOutputStream outStream) {
		/*
		try {
			Utilities.writeStringArray(outStream,
					strings.toArray(new String[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

	@Override
	public void copyFrom(FCDataInputStream inStream) {
		/*
		try {
			strings = new ArrayList<String>(
					Arrays.asList(Utilities.readStringArray(inStream)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
	}

	@Override
	public synchronized void copyFrom(SwirldState old) {
		super.copyFrom(old);
		documents = Collections.synchronizedList(new ArrayList<String>(((SocketDemoState) old).documents));
	}

	@Override
	public synchronized void handleTransaction(long id, boolean consensus,
			Instant timeCreated, byte[] transaction, Address address) {
		
		super.handleTransaction(id, consensus, timeCreated, transaction, address);		
	}

	@Override
	public void noMoreTransactions() {
	}

	@Override
	public synchronized void init(Platform platform, AddressBook addressBook) {
		super.init(platform, addressBook);
	}
}	