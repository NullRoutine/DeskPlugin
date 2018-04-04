package com.example.administrator.deskplugin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.deskplugin.Book;
import com.example.administrator.deskplugin.IMyAidlInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务端的AIDLService.java
 * Created by Administrator on 2018/4/4 0004.
 */

public class BookService extends Service {

    public final String TAG = this.getClass().getSimpleName();
    private List<Book> bookList = new ArrayList<>();

    private IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
        @Override
        public List<Book> getBook() throws RemoteException {
            synchronized (this) {
                Log.e(TAG, "invoking getBooks() method , now the list is : " + bookList.toString());
                if (bookList != null) {
                    return bookList;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this) {
                if (bookList == null) {
                    bookList = new ArrayList<>();
                }
                if (book == null) {
                    Log.e(TAG, "Book is null in In");
                    book = new Book();
                }
                //尝试修改book的参数，主要是为了观察其到客户端的反馈
                book.setPrice(2333);
                if (!bookList.contains(book)) {
                    bookList.add(book);
                }
                //打印bookList列表，观察客户端传过来的值
                Log.e(TAG, "invoking addBooks() method , now the list is : " + bookList.toString());
            }
        
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Book book = new Book();
        book.setName("Android开发艺术探索");
        book.setPrice(28);
        bookList.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(getClass().getSimpleName(), String.format("on bind,intent = %s", intent.toString()));
        return stub;
    }
}
