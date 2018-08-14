package com.billy.controller.lib.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.billy.controller.lib.DebugController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author billy.qi
 * @since 17/5/25 14:56
 */
public class ClientConnectionService extends Service {
    private static final String STOP_FLAG = "stop_send_msg";
    static AtomicBoolean running = new AtomicBoolean();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setRunning(false);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && running.compareAndSet(false, true)) {
            String ip = intent.getStringExtra("ip");
            int port = intent.getIntExtra("port", -1);
            SendThread sendThread = new SendThread(ip, port);
            sendThread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        setRunning(false);
        super.onDestroy();
    }

    private void setRunning(boolean value) {
        running.set(value);
    }

    private class SendThread extends Thread {
        private String ip;
        private int port;

        SendThread(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            setRunning(true);
            try{
                send();
            } catch(Exception e) {
                e.printStackTrace();
            }
            setRunning(false);
            DebugController.onConnectionStop();
            stopSelf();
        }

        private void send() {
            PrintWriter out = null;
            Socket socket = null;
            ClientMessageCache.clear();
            try{
                socket = new Socket(ip, port);
                new ReceiveThread(socket).start();
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream())), true);
                String msg;
                DebugController.onConnectionStart(getApplicationContext());
                while(running.get() && (msg = ClientMessageCache.get()) != null) {
                    if (STOP_FLAG.equals(msg)) {
                        break;
                    }
                    out.println(msg);
                }
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                if(out != null) try {out.close();} catch(Exception e) {e.printStackTrace();}
                if(socket != null) try {socket.close();} catch(Exception e) {e.printStackTrace();}
            }
            ClientMessageCache.clear();
        }
    }

    //用于接收服务端的信息
    private class ReceiveThread extends Thread {
        Socket socket;
        BufferedReader reader;

        ReceiveThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                receive();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (running.getAndSet(false)) {
                    ClientMessageCache.put(STOP_FLAG);//解决MessageCache.get()的阻塞
                }
            }
        }

        private void receive() throws IOException {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg;
            while((msg = reader.readLine()) != null) {
                DebugController.onMessage(msg);
            }
        }
    }


}
