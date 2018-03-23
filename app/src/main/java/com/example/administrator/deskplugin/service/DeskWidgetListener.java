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
        deskWidgetReceiver = new DeskWidgetReceiver();
    }

    public void setDeskWidgetReceiver(DeskWidgetReceiver deskWidgetReceiver) {
        this.deskWidgetReceiver = deskWidgetReceiver;
    }

    public void setScreenListener(ScreenListener screenListener) {
        this.screenListener = screenListener;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.SCREEN_ON");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        context.registerReceiver(deskWidgetReceiver, intentFilter);
    }

    public interface ScreenListener {
        void screenOn();

        void screenOff();
    }

    class DeskWidgetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (screenListener != null) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    screenListener.screenOff();
                } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    screenListener.screenOn();
                }
            }
        }
    }

}
