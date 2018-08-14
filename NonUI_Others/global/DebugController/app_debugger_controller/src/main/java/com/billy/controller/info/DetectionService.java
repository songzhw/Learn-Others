package com.billy.controller.info;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

/**
 * @author billy.qi
 * @since 17/9/26 19:52
 */
public class DetectionService extends AccessibilityService {

    final static String TAG = "DetectionService";

    static String foregroundPackageName;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId); // 根据需要返回不同的语义值
    }


    /**
     * 重载辅助功能事件回调函数，对窗口状态变化事件进行处理
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            InfoPanel.Info info = new InfoPanel.Info();
            info.packageName = event.getPackageName().toString();
            info.topActivity = event.getClassName().toString();
            InfoPanel.refresh(info);
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected  void onServiceConnected() {
        super.onServiceConnected();
    }
}