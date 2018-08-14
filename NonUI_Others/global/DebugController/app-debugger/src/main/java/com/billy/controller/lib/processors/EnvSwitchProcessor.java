package com.billy.controller.lib.processors;

import android.content.Context;
import android.text.TextUtils;

import com.billy.controller.lib.core.AbstractMessageProcessor;
import com.billy.controller.lib.core.PreferenceUtil;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * 环境切换
 * @author billy.qi
 * @since 17/6/14 15:59
 */
public class EnvSwitchProcessor extends AbstractMessageProcessor {
    private static final String FILE_NAME = "bl_shared_preference_name";

    private static PreferenceUtil PREFERENCE = new PreferenceUtil(FILE_NAME, Context.MODE_PRIVATE);

    private static final String KEY_GET = "get";
    private static final String KEY_PUT = "put";

    @Override
    public void onMessage(String message) {
        if (!TextUtils.isEmpty(message)) {
            if (message.startsWith(KEY_GET)) {
                message = message.substring(KEY_GET.length());
                sendCurEnvToServer(message);
            } else if (message.startsWith(KEY_PUT)) {
                message = message.substring(KEY_PUT.length());
                saveEnvFromServer(message);
            }
        }
    }

    private void saveEnvFromServer(String message) {
        if (!TextUtils.isEmpty(message)) {
            try{
                JSONObject json = new JSONObject(message);
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = json.getString(key);
                    if (value == null) {
                        value = "";
                    }
                    PREFERENCE.value(key, "").put(value);
                    if ("bl_network_env_type".equals(key)) {
                        clearNetworkCache();
                    }
                }
                sendMessage(json.toString());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clearNetworkCache() {
        try{
            Class<?> clazz = Class.forName("cn.com.bailian.plugin.utils.BhCacheUtils");
            Method method = clazz.getDeclaredMethod("clearCache");
            method.invoke(null);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getKey() {
        return "evn_switch";
    }

    @Override
    public void onConnectionStart(Context context) {
        PREFERENCE.init(context);
    }

    private void sendCurEnvToServer(String message) {
        if (!TextUtils.isEmpty(message)) {
            try{
                JSONObject json = new JSONObject(message);
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    json.putOpt(key, PREFERENCE.value(key, "").get());
                }
                sendMessage(json.toString());
            } catch(Exception e) {
                e.printStackTrace();
                sendMessage("error");
            }
        }
    }

    @Override
    public void onConnectionStop() {

    }
}
