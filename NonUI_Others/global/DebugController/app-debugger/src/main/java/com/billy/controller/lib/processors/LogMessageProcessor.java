package com.billy.controller.lib.processors;

import android.content.Context;

import com.billy.controller.lib.core.AbstractMessageProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Logcat类消息处理器
 * @author billy.qi
 * @since 17/5/31 15:01
 */
public class LogMessageProcessor extends AbstractMessageProcessor {

    private boolean start;

    @Override
    public void onMessage(String message) {

    }

    @Override
    public String getKey() {
        return "log";
    }

    @Override
    public void onConnectionStart(Context context) {
        //防止服务端反复开启/关闭连接，导致日志重复发送
        if (!start) {
            start = true;
            new LogThread().start();
        }
    }

    @Override
    public void onConnectionStop() {
    }

    private class LogThread extends Thread {
        @Override
        public void run() {
            Process pro = null;
            try {
                Runtime.getRuntime().exec("logcat -c").waitFor();
                pro = Runtime.getRuntime().exec("logcat");
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }

            if (pro == null) {
                if (isRunning()) {
                    sendMessage("get_logcat_failed");
                }
                return;
            }
            BufferedReader reader = null;
            try{
                reader = new BufferedReader(new InputStreamReader(pro.getInputStream()));
                String line;
                sendMessage("----start logging");
                while (isRunning()) {
                    try {
                        while (isRunning() && (line = reader.readLine()) != null) {
                            sendMessage(line);
                            Thread.yield();
                        }
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                sendMessage("----stop logging");
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                start = false;
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pro.destroy();
                }
            }
        }
    }
}
