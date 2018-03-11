package org.me.appstore;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

/**
 * Created by user on 2018/2/28.
 */

public class MyApplication extends Application {
    private static Context context;
    private static Handler handler;

    public static Handler getHandler() {
        return handler;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        Log.i("d", "dddddd");
        context = getApplicationContext();
        handler = new Handler();
    }
}
