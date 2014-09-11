package com.example.admin.labs;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Created by admin on 11.09.2014.
 */
public class LabsApp extends Application {
    public static String LABS_TAG = "LABS_TAG";
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(LABS_TAG, "config changed");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(LABS_TAG, "app created");

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(LABS_TAG,"low memory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(LABS_TAG,"app terminated");
    }
}
