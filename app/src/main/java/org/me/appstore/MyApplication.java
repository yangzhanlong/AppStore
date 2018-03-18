package org.me.appstore;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;

/**
 * Created by user on 2018/2/28.
 */

public class MyApplication extends Application {
    private static Context context;
    private static Handler handler;
    private static HashMap<String, String> dataCache;
    private static Gson gson;

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return context;
    }

    public static HashMap<String, String> getDataCache() {
        return dataCache;
    }

    public static Gson getGson() {
        return gson;
    }

    @Override
    public void onCreate() {
        Log.i("d", "dddddd");
        context = getApplicationContext();
        handler = new Handler();
        dataCache = new HashMap<>();
        gson = new Gson();
    }
}
