package ipos.hashgraph.model;

import java.io.Serializable;
import java.util.List;

public class Documents implements Serializable {

    private List<String> hash;

    public List<String> getHash() {
        return hash;
    }

    public void setHash(List<String> hash) {
        this.hash = hash;
    }
}
