package com.lee.project.application;

import android.app.Application;
import android.content.Context;

/**
 * @author lixuce
 */
public class App extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
