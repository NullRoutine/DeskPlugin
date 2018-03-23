package com.example.administrator.deskplugin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.deskplugin.service.AutoUpdateService;
import com.example.administrator.deskplugin.util.ScreenManager;

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
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
        ScreenManager.getInstance(this).setActivity(this);
        checkScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkScreen();
    }

    /**
     * 开启
     */
    public static void launch(Context context) {
        Intent intent = new Intent(context, KeepAliveActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 关闭保活页面
     */
    public static void kill(Context context) {
        if (keepAliveActivity != null) {
            keepAliveActivity.finish();
            Intent intent = new Intent(context, AutoUpdateService.class);
            context.startService(intent);
        }
    }

    /**
     * 检查屏幕状态  isScreenOn为true  屏幕“亮”结束该Activity
     */
    private void checkScreen() {
        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        boolean isScreenOn = pm.isScreenOn();
        if (isScreenOn) {
            finish();
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
