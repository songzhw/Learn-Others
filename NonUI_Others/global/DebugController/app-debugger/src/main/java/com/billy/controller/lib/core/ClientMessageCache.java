package com.billy.controller.lib.core;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author billy.qi
 * @since 17/5/25 16:43
 */
class ClientMessageCache {


    private static final LinkedBlockingQueue<String> cache = new LinkedBlockingQueue<>(1000);

    static void put(String content) {
        if (content != null) {
            boolean offer = cache.offer(content);
            if (!offer) {
                Log.w("ClientMessageCache", "cache is too large, content skipped:" + content);
            }
        }
    }

    static String get() throws InterruptedException {
        return cache.take();
    }

    static void clear() {
        cache.clear();
    }

}
