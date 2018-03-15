package simararora.ravenlib;

import simararora.ravenlib.model.RavenResource;

/**
 * Created by Simar Arora on 15/03/18.
 */

public interface ParseCompleteListener {

    void onParseComplete(RavenResource ravenResource);
    void onParseFailed(Exception exception);
}
