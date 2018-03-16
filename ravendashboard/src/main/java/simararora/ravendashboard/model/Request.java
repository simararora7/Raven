package simararora.ravendashboard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class Request {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("data")
    @Expose
    private Map<String, String> data;

    //Used in case of Resource
    public Request(String type, Map<String, String> data) {
        this.type = type;
        this.data = data;
    }

    //Used in case of Source
    public Request(Map<String, String> data) {
        this.data = data;
    }

    public Request() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
