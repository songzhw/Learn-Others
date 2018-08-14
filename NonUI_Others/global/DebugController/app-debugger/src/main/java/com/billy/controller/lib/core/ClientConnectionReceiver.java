package com.billy.controller.lib.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * @author billy.qi
 * @since 17/5/25 13:15
 */
public class ClientConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String ip = intent.getStringExtra("ip");
        int port = intent.getIntExtra("port", -1);
        if (!TextUtils.isEmpty(ip) && port > 0) {
            Intent serviceIntent = new Intent(context, ClientConnectionService.class);
            serviceIntent.putExtras(intent);
            context.startService(serviceIntent);
        }
    }
}
