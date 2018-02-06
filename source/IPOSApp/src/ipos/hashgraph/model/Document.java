package ipos.hashgraph.model;

import java.io.Serializable;

public class Document implements Serializable {

    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
