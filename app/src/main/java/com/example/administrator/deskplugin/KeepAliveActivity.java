package com.example.administrator.deskplugin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.deskplugin.util.ScreenManager;
import com.example.administrator.deskplugin.util.SystemUtils;

/**
 * 一像素保活页面
 * Created by Administrator on 2018/3/23 0023.
 */

public class KeepAliveActivity extends AppCompatActivity {

    private static KeepAliveActivity keepAliveActivity;

    @SuppressLint("RtlHardcoded")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keepAliveActivity = this;
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        Log.e("DeskPlugin", "=======>");
        ScreenManager.getInstance(this).setActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("DeskPlugin", "进程" + SystemUtils.isAPPALive(this, getPackageName()));
        if (!SystemUtils.isAPPALive(this, getPackageName())) {
            Intent intentAlive = new Intent(this, MainActivity.class);
            intentAlive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentAlive);
        }
    }
}
