package com.example.newbiechen.ireader;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.example.newbiechen.ireader.service.DownloadService;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by newbiechen on 17-4-15.
 */

public class App extends Application {
    private static Context sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        startService(new Intent(getContext(), DownloadService.class));

        // 初始化内存分析工具
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }
    }

    public static Context getContext(){
        return sInstance;
    }
}