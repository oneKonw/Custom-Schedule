package com.example.customschedule;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by hyt on 2018/3/9.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;

    }
}
