package com.billy.controller.core;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.billy.controller.R;
import com.billy.controller.util.PreferenceUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.billy.controller.core.ConnectionStatus.RUNNING;
import static com.billy.controller.core.ConnectionStatus.STARTING;
import static com.billy.controller.core.ConnectionStatus.STOPPED;
import static com.billy.controller.core.ConnectionStatus.STOPPING;
import static com.billy.controller.core.ConnectionStatus.WAITING_CLIENT;

/**
 * @author billy.qi
 * @since 17/5/26 13:02
 */
public class ServerConnectionService extends Service {

    private static final long WAITING_CLIENT_REPEAT_TIME = 1000;//1s 重复发送广播间隔
    private static AtomicBoolean running = new AtomicBoolean();
    ExecutorService mExecutorService;  //create a thread pool
    public static final String IP = "127.0.0.1";
    public static final int PORT = 9099;
    public static final int CLIENT_COUNT = 1;//只接受指定数量的客户端
    private static final String TAG = "ServerConnectionService";
    private ServerSocket ss = null;
    private Socket client = null;
    private BufferedReader in = null;
    private ConnectionStatus status;
    private Handler handler;
    private BufferedWriter out;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mExecutorService = Executors.newSingleThreadExecutor();
        setStatus(STOPPED);
        setRunning(false);
        handler = new Handler();
    }

    @Override
    public void onDestroy() {
        if (mExecutorService != null && !mExecutorService.isShutdown()) {
            mExecutorService.shutdownNow();
        }
        closeSocket();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            boolean stop = intent.getBooleanExtra("stop", false);
            if (stop) {
                stopConnection();
            } else {
                if (running.compareAndSet(false, true)) {
                    logcat("set running = true");
                    new StartConnectionThread().start();
                } else {
                    setStatus(status);
                }
                handler.postDelayed(sendConnectBroadcast, 300);//持续探测主机
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    
    Runnable sendConnectBroadcast = new Runnable() {
        @Override
        public void run() {
            //未连接之前反复探测主机. 若已经找到, status就不会是wating_client了
            if (status == WAITING_CLIENT) {
                String action = getString(R.string.log_action, PreferenceUtil.getString(PreferenceUtil.KEY_PACKAGE_NAME, ""));
                Intent intent = new Intent(action);
                intent.putExtra("ip", IP);
                intent.putExtra("port", PORT);
                sendBroadcast(intent, "com.billy.controller.broadcast.debugger");
                handler.postDelayed(sendConnectBroadcast, WAITING_CLIENT_REPEAT_TIME); //第二参1000ms
            }
        }
    };

    private void setRunning(boolean value) {
        running.set(value);
        logcat("set running = " + value);
    }

    private void logcat(String message) {
        Log.i(TAG, message);
    }

    private void setStatus(ConnectionStatus st) {
        status = st;
        logcat("status:" + st.toString());
        ServerMessageProcessorManager.onStatus(st);
    }

    private void processMessage(String msg) {
        //single的异步线程执行保证msg的处理顺序
        mExecutorService.execute(new ProcessMessageTask(msg));
    }

    private class ProcessMessageTask implements Runnable {
        String message;

        ProcessMessageTask(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            ServerMessageProcessorManager.onMessage(message);
        }
    }


    private void closeSocket() {
        if (client != null) {
            try{
                client.shutdownInput();//关闭输入流，让in.readLine()阻塞中止
                client.shutdownOutput();//关闭输出流，让客户端的in.readLine()阻塞中止
            } catch(Exception ignored) {
            }
        }
        close(in);
        close(client);
        close(ss);
        close(out);
        in = null;
        client = null;
        ss = null;
        out = null;
        setStatus(STOPPED);
        setRunning(false);
        ServerMessageProcessorManager.sendMessageToBreakMessageCacheGetMethod();
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try{
                closeable.close();
            } catch(Exception ignored) {
            }
        }
    }

    private void stopConnection() {
        setStatus(STOPPING);
        if (status == WAITING_CLIENT) {
            //通过创建一个无用的client来让ServerSocket跳过accept阻塞，从而中止
            Socket socket = null;
            try {
                socket = new Socket(IP, PORT);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(socket);
            }
        }
        closeSocket();
    }

    private class StartConnectionThread extends Thread {

        @Override
        public void run() {
            if (status == STOPPED) {
                try {
                    setStatus(STARTING);
                    startConnection();
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    closeSocket();
                }
            }
            stopSelf();
        }

        private void startConnection() throws IOException {
            //创建一个ServerSocket ，监听客户端socket的连接请求
            ss = new ServerSocket(PORT, CLIENT_COUNT);
            in = null;
            out = null;
            String msg;
            setStatus(WAITING_CLIENT);
            client = ss.accept();
            if (status == WAITING_CLIENT) {
                setStatus(RUNNING);

                out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                new SendMessageThread().start();

                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while(status == RUNNING && (msg = in.readLine()) != null) {
                    processMessage(msg);
                }

            }
        }
    }

    private class SendMessageThread extends Thread {

        @Override
        public void run() {
            try{
                sendCachedMessage();
            } catch(Exception e) {
                e.printStackTrace();
            } finally {
                ServerMessageCache.clear();
            }
        }

        private void sendCachedMessage() {
            String msg;
            while (out != null && running.get()) {
                try{
                    while(running.get() && (msg = ServerMessageCache.get()) != null) {
                        if (ServerMessageProcessorManager.SEPARATOR.equals(msg)) {
                            break;
                        }
                        out.write(msg);
                        out.newLine();
                        out.flush();
                    }
                } catch(Exception ignored) {
                }
            }
        }
    }
}
