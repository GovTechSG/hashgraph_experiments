package ipos.hashgraph;

import com.swirlds.platform.*;
import com.txmq.exo.core.ExoState;
import com.txmq.exo.messaging.ExoMessage;
import ipos.hashgraph.model.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IPOSAppState extends ExoState implements SwirldState {

    private List<ExoMessage> documents = Collections.synchronizedList(new ArrayList<ExoMessage>());

    public synchronized List<ExoMessage> getDocuments() {
        return documents;
    }

    public synchronized void addDocument(ExoMessage document) {
        this.documents.add(document);
    }

    @Override
    public synchronized AddressBook getAddressBookCopy() {
        return addressBook.copy();
    }

    @Override
    public synchronized FastCopyable copy() {
        IPOSAppState copy = new IPOSAppState();
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
        documents = Collections.synchronizedList(new ArrayList<>(((IPOSAppState) old).documents));
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