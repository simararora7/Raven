package simararora.ravendashboard.model;

/**
 * Created by nateshrelhan on 3/16/18.
 */

public class History {
    private String encodedKey;
    private String value;

    public History(String encodedKey, String value) {
        this.encodedKey = encodedKey;
        this.value = value;
    }

    public String getEncodedKey() {
        return encodedKey;
    }

    public void setEncodedKey(String encodedKey) {
        this.encodedKey = encodedKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
