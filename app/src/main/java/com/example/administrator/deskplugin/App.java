package com.example.administrator.deskplugin;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class App extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
    }
    public static Context getInstance(){
        return context;
    }
}
