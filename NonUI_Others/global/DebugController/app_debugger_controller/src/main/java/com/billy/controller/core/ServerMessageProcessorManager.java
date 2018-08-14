package com.billy.controller.core;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author billy.qi
 * @since 17/5/29 19:07
 */
public class ServerMessageProcessorManager {
    static final String SEPARATOR = ":";
    public static final String KEY_STATUS = "status";
    public static final String KEY_LOG = "log";

    private static HashMap<String, List<IServerMessageProcessor>> allProcessors = new HashMap<>();

    public static void addProcessor(IServerMessageProcessor processor) {
        if (processor != null && processor.getDebugKey() != null) {
            String debugKey = processor.getDebugKey();
            List<IServerMessageProcessor> processors = allProcessors.get(debugKey);
            if (processors == null) {
                processors = new LinkedList<>();
                allProcessors.put(debugKey, processors);
            }
            if (!processors.contains(processor)) {
                processors.add(processor);
            }
        }
    }

    public static void removeProcessor(IServerMessageProcessor processor) {
        if (processor != null && processor.getDebugKey() != null) {
            List<IServerMessageProcessor> processors = allProcessors.get(processor.getDebugKey());
            if (processors != null) {
                if (processors.contains(processor)) {
                    processors.remove(processor);
                }
            }
        }
    }

    public static void clear() {
        allProcessors.clear();
    }

    static void onStatus(ConnectionStatus status) {
        Set<String> keySet = allProcessors.keySet();
        for (String key : keySet) {
            List<IServerMessageProcessor> list = allProcessors.get(key);
            if (list != null && !list.isEmpty()) {
                for (IServerMessageProcessor processor : list) {
                    processor.onStatus(status);
                }
            }
        }
    }

    static void onMessage(String message) {
        if (message != null) {
            int index = message.indexOf(SEPARATOR);
            if (index >= 0 && message.length() > index + 1) {
                String key = message.substring(0, index);
                String content = message.replaceFirst(key + SEPARATOR, "");
                List<IServerMessageProcessor> list = allProcessors.get(key);
                if (list != null && !list.isEmpty()) {
                    for (IServerMessageProcessor processor : list) {
                        processor.onMessage(content);
                    }
                }
            }
        }
    }

    public static void sendMessageToClient(IServerMessageProcessor processor, String message) {
        if (processor != null && !TextUtils.isEmpty(message)) {
            String key = processor.getDebugKey();
            if (!TextUtils.isEmpty(key)) {
                senMessageToClient(key, message);
            }
        }
    }

    public static void senMessageToClient(String key, String message) {
        ServerMessageCache.put(key + SEPARATOR + message);
    }

    static void sendMessageToBreakMessageCacheGetMethod() {
        ServerMessageCache.put(SEPARATOR);
    }

}
