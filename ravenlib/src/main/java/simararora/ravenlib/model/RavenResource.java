package simararora.ravenlib.model;

import android.net.Uri;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.Iterator;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class RavenResource {
    private String resourceType;
    private String resourceId;
    private String sourceId;
    private JSONObject resourceIdParams;
    private JSONObject sourceIdParams;
    private Uri uri;

    public RavenResource(String path) throws Exception{
        //Path is of the type /ab/abcde/abc
        //Where ab is resourceType
        //abcde is resourceId
        //abc is sourceId
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

    public void setResourceIdParams(JSONObject resourceIdParams) {
        this.resourceIdParams = resourceIdParams;
    }

    public JSONObject getSourceIdParams() {
        return sourceIdParams;
    }

    public void setSourceIdParams(JSONObject sourceIdParams) {
        this.sourceIdParams = sourceIdParams;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        try {
            return "<b>RavenResource</b><br>" +
                    "<br><b>resourceType:</b> " + resourceType +
                    "<br><b>resourceId:</b> " + resourceId +
                    "<br><b>sourceId:</b> " + sourceId +
                    mapToString(resourceIdParams) +
                    mapToString(sourceIdParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String mapToString(JSONObject jsonObject) throws Exception{
        StringBuilder s = new StringBuilder();
        Iterator<String> keys = jsonObject.keys();
        while( keys.hasNext() ) {
            String key = keys.next();
            s.append(String.format("<br><b>%s:</b> ", key)).append(jsonObject.get(key).toString());
        }
        return s.toString();
    }
}
