package com.example.administrator.deskplugin.util;

import android.app.Activity;
import android.content.Context;

import com.example.administrator.deskplugin.KeepAliveActivity;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class ScreenManager {
    private Context mContext;
    private WeakReference<Activity> mActivityWref;
    public static ScreenManager gDefualt;

    public static ScreenManager getInstance(Context pContext) {
        if (gDefualt == null) {
            gDefualt = new ScreenManager(pContext.getApplicationContext());
        }
        return gDefualt;
    }

    private ScreenManager(Context pContext) {
        this.mContext = pContext;
    }

    public void setActivity(Activity pActivity) {
        mActivityWref = new WeakReference<Activity>(pActivity);
    }

    public void startActivity() {
        KeepAliveActivity.launch(mContext);
    }

    public void finishActivity() { //结束掉LiveActivity
        if (mActivityWref != null) {
            Activity activity = mActivityWref.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
