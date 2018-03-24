package com.example.administrator.deskplugin.service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RemoteViews;

import com.example.administrator.deskplugin.R;
import com.example.administrator.deskplugin.util.Lunar;
import com.example.administrator.deskplugin.util.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 服务，为了更新时间
 * Created by Administrator on 2018/3/23 0023.
 */

public class AutoUpdateService extends Service {

    private static final int TIME_FORMAT_24 = 0;
    private static final int TIME_FORMAT_AM = 1;
    private static final int TIME_FORMAT_PM = 2;
    // 广播接收者去接收系统每分钟的提示广播，来更新时间
    private BroadcastReceiver mTimePickerBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            upTime();
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                context.startService(new Intent(context, AutoUpdateService.class));
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        upTime();
        registerReceiver();
        return START_STICKY;
    }

    private void upTime() {
        RemoteViews remoteViews = new RemoteViews(getApplication().getPackageName(),
                R.layout.desk_widget);// 实例化RemoteViews
        int timeFormat = getTimeFormat();
        // 定义SimpleDateFormat对象
        SimpleDateFormat df = new SimpleDateFormat("hh:mm", Locale.CHINA);
        if (timeFormat == TIME_FORMAT_24) {
            df = new SimpleDateFormat("HH:mm", Locale.CHINA);
        }
        // 将当前时间格式化成HHmm的形式
        String timeStr = df.format(new Date(System.currentTimeMillis()));
        remoteViews.setTextViewText(R.id.tv_time, timeStr);
        if (timeFormat == 0) {
            remoteViews.setViewVisibility(R.id.tv_am, View.GONE);
        }
        if (timeFormat == 1) {
            remoteViews.setViewVisibility(R.id.tv_am, View.VISIBLE);
            remoteViews.setTextViewText(R.id.tv_am, "上午");
        } else if (timeFormat == 2) {
            remoteViews.setViewVisibility(R.id.tv_am, View.VISIBLE);
            remoteViews.setTextViewText(R.id.tv_am, "下午");
        }
        remoteViews.setTextViewText(R.id.tv_date, TimeUtil.getZhouWeek());
        remoteViews.setTextViewText(R.id.tv_long, Lunar.getDay());
        ComponentName componentName = new ComponentName(getApplication(),
                DeskWidgetProvider.class);
        AppWidgetManager.getInstance(getApplication()).updateAppWidget(
                componentName, remoteViews);
    }

    private void registerReceiver() {
        IntentFilter updateIntent = new IntentFilter();
        updateIntent.addAction(Intent.ACTION_TIME_TICK);//检测系统时间变化的action
        updateIntent.addAction(Intent.ACTION_TIME_CHANGED);
        updateIntent.addAction(Intent.ACTION_DATE_CHANGED);
        updateIntent.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver(mTimePickerBroadcast, updateIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 获取时间是24小时制还是12小时制
     */
    private int getTimeFormat() {
        ContentResolver cv = this.getContentResolver();
        String strTimeFormat = android.provider.Settings.System.getString(cv,
                android.provider.Settings.System.TIME_12_24);
        if (strTimeFormat != null) {
            if (strTimeFormat.equals("24")) {
                return TIME_FORMAT_24;
            } else {
                // String amPmValues;
                Calendar c = Calendar.getInstance();
                if (c.get(Calendar.AM_PM) == 0) {
                    // amPmValues = "AM";
                    return TIME_FORMAT_AM;
                } else {
                    // amPmValues = "PM";
                    return TIME_FORMAT_PM;
                }
            }
        }
        return TIME_FORMAT_24;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTimePickerBroadcast != null) {
            unregisterReceiver(mTimePickerBroadcast);//解除广播
        }
    }
}
