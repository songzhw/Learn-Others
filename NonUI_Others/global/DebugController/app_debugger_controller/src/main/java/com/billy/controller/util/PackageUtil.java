package com.billy.controller.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;

import com.billy.controller.MyApp;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author billy.qi
 * @since 17/6/1 22:53
 */
public class PackageUtil {

    public static String[] getPackageNameList() {
        //获取手机中所有已安装的应用，并判断是否系统应用
        ArrayList<String> appList = new ArrayList<>(); //用来存储获取的应用信息数据，手机上安装的应用数据都存在appList里
        List<PackageInfo> packages = MyApp.get().getPackageManager().getInstalledPackages(0);

        for(int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            //判断是否系统应用
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //非系统应用
                appList.add(packageInfo.packageName);
            }
        }
        return appList.toArray(new String[]{});
    }

    public static String getAppName(String packageName) {
        if (!TextUtils.isEmpty(packageName)) {
            //获取手机中所有已安装的应用，并判断是否系统应用
            PackageManager packageManager = MyApp.get().getPackageManager();
            List<PackageInfo> packages = packageManager.getInstalledPackages(0);
            for(int i = 0; i < packages.size(); i++) {
                PackageInfo packageInfo = packages.get(i);
                if(packageName.equals(packageInfo.packageName)) {
                    return packageInfo.applicationInfo.loadLabel(packageManager).toString();
                }
            }
        }
        return null;
    }

    public static String getMd5SignByPackageName(String packageName) {
        try{
            PackageInfo packageInfo = MyApp.get().getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            Signature signature = signatures[0];
            return md5(signature);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String md5(Signature signature) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    signature.toByteArray());
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10)
                    hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
