package com.example.administrator.deskplugin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.administrator.deskplugin.service.DeskWidgetListener;
import com.example.administrator.deskplugin.util.ScreenManager;

public class MainActivity extends AppCompatActivity implements DeskWidgetListener.ScreenListener {

    // 动态注册锁屏等广播
    private DeskWidgetListener mDeskWidgetListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mScreenManager = ScreenManager.getInstance(this);
        mDeskWidgetListener = new DeskWidgetListener(this);
        mDeskWidgetListener.setScreenListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDeskWidgetListener.stopScreenReceiverListener();
    }

    @Override
    public void screenOn() {
        mScreenManager.finishActivity();
    }

    @Override
    public void screenOff() {
        mScreenManager.startActivity();
    }

    @Override
    public void userPresent() {

    }
}
