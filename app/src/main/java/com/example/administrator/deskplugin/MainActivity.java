package com.example.administrator.deskplugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.deskplugin.service.DeskWidgetListener;
import com.example.administrator.deskplugin.util.ScreenManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements DeskWidgetListener.ScreenListener, View.OnClickListener {

    // 动态注册锁屏等广播
    private DeskWidgetListener mDeskWidgetListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;

    //由AIDL文件生成的Java类
    private IMyAidlInterface mBookManager = null;

    //标志当前与服务端连接状况的布尔值，false为未连接，true为连接中
    private boolean mBound = false;

    //包含Book对象的list
    private List<Book> mBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        findViewById(R.id.tv_add).setOnClickListener(this);
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
    protected void onStart() {
        super.onStart();
        if (!mBound) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mServiceConnection);
            mBound = false;
        }
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

    /**
     * 尝试与服务端建立连接
     */
    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setAction("com.example.administrator.deskplugin.client");
        intent.setPackage("com.example.administrator.deskplugin");//这里注意要写包名
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e(getLocalClassName(), "service connected");
            mBookManager = IMyAidlInterface.Stub.asInterface(service);
            mBound = true;

            if (mBookManager != null) {
                try {
                    mBooks = mBookManager.getBook();
                    Log.e(getLocalClassName(), mBooks.toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(getLocalClassName(), "service disconnected");
            mBound = false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                //如果与服务端的连接处于未连接状态，则尝试连接
                if (!mBound) {
                    attemptToBindService();
                    Toast.makeText(this, "当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mBookManager == null) return;

                Book book = new Book();
                book.setName("APP研发录In");
                book.setPrice(30);
                try {
                    mBookManager.addBook(book);
                    mBookManager.getBook();
                    Log.e(getLocalClassName(), book.toString() + "++++=>" + mBookManager.getBook().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
