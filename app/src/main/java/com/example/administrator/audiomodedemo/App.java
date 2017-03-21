package com.example.administrator.audiomodedemo;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * ================================================
 * 作    者：JayGoo
 * 版    本：1.1.0
 * 创建日期：2017/1/6
 * 描    述:
 * ================================================
 */
public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


    }

    /**
     * 获取APP的Context方便其他地方调用
     * @return
     */
    public static Context getInstance(){
        return instance;
    }


}