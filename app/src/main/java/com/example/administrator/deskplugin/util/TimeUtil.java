package com.example.administrator.deskplugin.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by twq on 2017/4/17.
 */

public class TimeUtil {
    private static final String[] WEEK = { "天", "一", "二", "三", "四", "五", "六" };
    public static final String XING_QI = "星期";
    public static final String ZHOU = "周";

    public static String getWeek(int num, String format) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int weekNum = c.get(Calendar.DAY_OF_WEEK) + num;
        if (weekNum > 7)
            weekNum = weekNum - 7;
        return format + WEEK[weekNum - 1];
    }

    public static String getZhouWeek() {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd");
        return format.format(new Date(System.currentTimeMillis())) + " "
                + getWeek(0, ZHOU);
    }

    public static String getDay(long timesamp) {
        String result = "";
        long timeGap = System.currentTimeMillis() - timesamp;
        if (timeGap > 24 * 3 * 60 * 60 * 1000) {
            result = timeGap / (24 * 60 * 60 * 1000) + "天前";
        } else if (timeGap > 24 * 2 * 60 * 60 * 1000) {
            result = "前天";
        } else if (timeGap > 24 * 60 * 60 * 1000) {
            result = "昨天";
        } else {
            result = "今天";
        }
        return result;
    }

    public static String getDayString(long timesamp) {
        String result = "";
        final Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendarToday.setTimeInMillis(System.currentTimeMillis());

        final Calendar calendarday = Calendar.getInstance();
        calendarday.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        calendarday.setTimeInMillis(timesamp);

        int time = calendarToday.DAY_OF_MONTH - calendarday.DAY_OF_MONTH;

        if (time == 2) {
            result = "前天";
        } else if (time == 1) {
            result = "昨天";
        } else if (time == 0) {
            result = "今天";
        } else {
            result = time + "天前";
        }

        return result;
    }
}
