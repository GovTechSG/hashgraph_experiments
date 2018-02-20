package com.txmq.socketdemo;


import com.swirlds.platform.*;
import com.txmq.exo.core.ExoState;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IPOSState extends ExoState implements SwirldState {


    private List<String> documents = Collections.synchronizedList(new ArrayList<String>());

    public synchronized List<String> getDocuments() {
        return documents;
    }

    public synchronized void addDocument(String name) {
        this.documents.add(name);
    }

    @Override
    public synchronized AddressBook getAddressBookCopy() {
        return addressBook.copy();
    }

    @Override
    public synchronized FastCopyable copy() {
        IPOSState copy = new IPOSState();
        copy.copyFrom(this);
        return copy;
    }

    @Override
    public void copyTo(FCDataOutputStream outStream) {
    }

    @Override
    public void copyFrom(FCDataInputStream inStream) {
    }

    @Override
    public synchronized void copyFrom(SwirldState old) {
        super.copyFrom(old);
        documents = Collections.synchronizedList(new ArrayList<>(((IPOSState) old).documents));
    }

    @Override
    public synchronized void handleTransaction(long id, boolean consensus,
                                               Instant timeCreated, byte[] transaction, Address address) {

        //TODO GEt the transactions from couchdb

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