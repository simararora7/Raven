package simararora.ravenlib.model;

import org.json.JSONObject;

import java.net.MalformedURLException;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class RavenResource {
    private String resourceType;
    private String resourceId;
    private String sourceId;
    private JSONObject resourceIdParams;
    private JSONObject sourceIdParams;

    public RavenResource(String path) throws Exception{
        String[] tokens = path.substring(1).split("/");
        if (tokens.length != 3)
            throw new MalformedURLException();
        resourceType = tokens[0];
        resourceId = tokens[1];
        sourceId = tokens[2];
    }

    public boolean isComplete() {
        return resourceIdParams != null && sourceIdParams != null;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public JSONObject getResourceIdParams() {
        return resourceIdParams;
    }

    public JSONObject getSourceIdParams() {
        return sourceIdParams;
    }
}
