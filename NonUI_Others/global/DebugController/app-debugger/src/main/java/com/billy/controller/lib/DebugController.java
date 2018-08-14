package com.billy.controller.lib;

import android.content.Context;
import android.text.TextUtils;

import com.billy.controller.lib.core.AbstractMessageProcessor;
import com.billy.controller.lib.processors.EnvSwitchProcessor;
import com.billy.controller.lib.processors.LogMessageProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.billy.controller.lib.core.AbstractMessageProcessor.MESSAGE_SEPARATOR;

/**
 * @author billy.qi
 * @since 17/5/31 14:33
 */
public class DebugController {

    private static final ExecutorService EXECUTORSERVICE = Executors.newSingleThreadExecutor();

    private static final List<AbstractMessageProcessor> PROCESSORS = new ArrayList<>();

    static {
        addProcessor(new LogMessageProcessor());//默认添加一个日志监控
        addProcessor(new EnvSwitchProcessor());//默认添加一个日志监控
    }

    public static void addProcessor(AbstractMessageProcessor processor) {
        if (processor != null && !PROCESSORS.contains(processor)) {
            PROCESSORS.add(processor);
        }
    }

    public static void removeProcessor(AbstractMessageProcessor processor) {
        if (processor != null && PROCESSORS.contains(processor)) {
            PROCESSORS.remove(processor);
        }
    }

    public static void onConnectionStart(Context applicationContext) {
        for (AbstractMessageProcessor processor : PROCESSORS) {
            processor.onConnectionStart(applicationContext);
        }
    }
    public static void onConnectionStop() {
        for (AbstractMessageProcessor processor : PROCESSORS) {
            processor.onConnectionStop();
        }
    }

    /**
     * 处理从服务端发送过来的消息
     * @param message 消息内容，格式为 key:value
     */
    public static void onMessage(String message) {
        for (AbstractMessageProcessor processor : PROCESSORS) {
            String key = processor.getKey();
            if (key != null) {
                String prefix = key + MESSAGE_SEPARATOR;
                if (message.startsWith(prefix)) {
                    message = message.substring(prefix.length());//去除prefix: ( key: )
                    EXECUTORSERVICE.execute(new ProcessMessageTask(processor, message));
                }
            }
        }
    }

    private static class ProcessMessageTask implements Runnable {
        AbstractMessageProcessor processor;
        String message;

        ProcessMessageTask(AbstractMessageProcessor processor, String message) {
            this.processor = processor;
            this.message = message;
        }
        @Override
        public void run() {
            if (processor != null && !TextUtils.isEmpty(message)) {
                processor.onMessage(message);
            }
        }
    }

}
