package ipos.hashgraph.model;

import java.io.Serializable;
import java.util.Map;

public class Document implements Serializable {

    private String hash;

    private String userId;

    private Map<String, String> meta;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
}


