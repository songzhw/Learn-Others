package com.billy.controller;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Process;

import java.util.List;

/**
 * @author billy.qi
 * @since 17/5/26 13:08
 */
public class MyApp extends Application {

    private static MyApp instance;

    public static MyApp get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (getPackageName().equals(getCurProcessName(this))) {//主进程
        }
    }


    public static String getCurProcessName(Application app){//进程是否以包名在运行（主进程是否运行）
        String processName = null;
        ActivityManager manager = (ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> list = manager.getRunningAppProcesses();
        int myPid = Process.myPid();
        for (RunningAppProcessInfo appProcess : list){
            if(appProcess.pid == myPid){
                processName = appProcess.processName;
                break;
            }
        }
        return processName;
    }
}
