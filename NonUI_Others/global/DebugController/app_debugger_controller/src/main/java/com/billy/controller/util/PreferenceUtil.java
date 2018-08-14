package com.billy.controller.util;

import android.content.SharedPreferences;

import com.billy.controller.MyApp;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author billy.qi
 * @since 17/6/1 14:44
 */
public class PreferenceUtil {
    public static final String KEY_PACKAGE_NAME = "packageName";


    public static String getString(String key, String defaultValue) {
        SharedPreferences cache = getSharedPreferences();
        return cache.getString(key, defaultValue);
    }

    public static void putString(String key, String value) {
        SharedPreferences cache = getSharedPreferences();
        SharedPreferences.Editor edit = cache.edit();
        edit.putString(key, value);
        edit.apply();
    }

    private static SharedPreferences getSharedPreferences() {
        return MyApp.get().getSharedPreferences("cache", MODE_PRIVATE);
    }

}
