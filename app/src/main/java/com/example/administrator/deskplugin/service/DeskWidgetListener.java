package com.example.administrator.deskplugin.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * 接收锁屏广播
 * Created by Administrator on 2018/3/23 0023.
 */
public class DeskWidgetListener {
    private ScreenListener screenListener;
    private Context context;
    private DeskWidgetReceiver deskWidgetReceiver;

    public DeskWidgetListener(Context context) {
        this.context = context;
        this.deskWidgetReceiver = new DeskWidgetReceiver();
    }

    public void setScreenListener(ScreenListener screenListener) {
        this.screenListener = screenListener;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        context.registerReceiver(deskWidgetReceiver, intentFilter);
    }

    public void stopScreenReceiverListener() {
        context.unregisterReceiver(deskWidgetReceiver);
    }

    public interface ScreenListener {
        void screenOn();

        void screenOff();

        void userPresent();
    }

    class DeskWidgetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (screenListener == null) {
                return;
            }
            if (Intent.ACTION_SCREEN_OFF.equals(action)) {//锁屏
                screenListener.screenOff();
            } else if (Intent.ACTION_SCREEN_ON.equals(action)) {//开屏
                screenListener.screenOn();
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {//解锁
                screenListener.userPresent();
            }

        }
    }

}
