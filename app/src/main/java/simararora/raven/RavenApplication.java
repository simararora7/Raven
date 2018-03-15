package simararora.raven;

import android.app.Application;

import simararora.ravenlib.Raven;

/**
 * Created by Simar Arora on 15/03/18.
 */

public class RavenApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Raven.init(this);
    }
}
