package com.billy.controller.info;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.billy.controller.R;

/**
 * @author billy.qi
 * @since 17/9/26 17:51
 */
public class InfoPanel {

    Application context;
    private LinearLayout floatLayout;

    private WindowManager.LayoutParams wmParams;
    private WindowManager mWindowManager;
    private TextView textView;
    private boolean shown;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static InfoPanel current;

    static class Info {

        String packageName;
        String topActivity;

        @Override
        public String toString() {
            return packageName + "\n" + topActivity;
        }
    }

    public InfoPanel(Application context) {
        current = this;
        this.context = context;
        mWindowManager = (WindowManager) context.getSystemService(Application.WINDOW_SERVICE);
        floatLayout = createFloatView(context);
        textView = (TextView) floatLayout.findViewById(R.id.detail_info);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;// 设置window
        // type为TYPE_SYSTEM_ALERT
        wmParams.format = PixelFormat.RGBA_8888;// 设置图片格式，效果为背景透明
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.gravity = Gravity.START | Gravity.TOP;// 默认位置：右上角
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.x = 10;// 设置x、y初始值，相对于gravity
        wmParams.y = 50;
    }

    /** 创建悬浮窗 */
    private LinearLayout createFloatView(Context context) {
        // 获取浮动窗口视图所在布局
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.ui_float_layout, null);
        layout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        // 设置监听浮动窗口的触摸移动
        layout.setOnTouchListener(new View.OnTouchListener() {
            float x, y;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // getRawX是触摸位置相对于屏幕的坐标，getX是相对于按钮的坐标
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        wmParams.x += event.getRawX() - x;
                        wmParams.y += event.getRawY() - y;
                        mWindowManager.updateViewLayout(floatLayout, wmParams);// 刷新
                        break;
                }
                x = event.getRawX();
                y = event.getRawY();
                return false; // 此处必须返回false，否则OnClickListener获取不到监听
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // do something... 跳转到应用
            }
        });
        return layout;
    }

    //刷新显示
    public static void refresh(Info info) {
        if (current == null || info == null) {
            return;
        }
        current.refreshRunnable.content = info.toString();
        current.handler.post(current.refreshRunnable);
    }
    private RefreshRunnable refreshRunnable = new RefreshRunnable();

    private class RefreshRunnable implements Runnable {
        String content;
        @Override
        public void run() {
            if (textView != null && content != null) {
                textView.setText(content);
            }
        }
    }


    public void destroy() {
        hide();
        current = null;
    }

    private boolean isShown() {
        if (shown) {
            if (floatLayout != null && mWindowManager != null) {
                return floatLayout.getParent() != null;
            }
        }
        return false;
    }

    public void toggleShow(Context context) {
        // 判断辅助功能是否开启
        if (!isAccessibilitySettingsOn(context)) {
            // 引导至辅助功能设置页面
            context.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        } else {
            if (isShown()) {
                hide();
            } else {
                show();
            }
        }
    }

    private void hide() {
        if (!isShown()) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (floatLayout != null && mWindowManager != null) {
                    mWindowManager.removeView(floatLayout);// 移除悬浮窗口
                }
            }
        });
        shown = false;
    }

    private void show() {
        if (isShown()) {
            return;
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (floatLayout != null && mWindowManager != null) {
                    mWindowManager.addView(floatLayout, wmParams);// 显示悬浮窗口
                }
            }
        });
        shown = true;
    }


    // 此方法用来判断当前应用的辅助功能服务是否开启
    public static boolean isAccessibilitySettingsOn(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (accessibilityEnabled == 1) {
            String services = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (services != null) {
                return services.toLowerCase().contains(context.getPackageName().toLowerCase());
            }
        }
        return false;
    }
}
