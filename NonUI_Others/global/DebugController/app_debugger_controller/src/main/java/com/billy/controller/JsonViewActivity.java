package com.billy.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.controller.util.JsonFormat;

/**
 * @author billy.qi
 * @since 17/5/30 15:13
 */
public class JsonViewActivity extends BaseActivity {

    public static final String EXTRA_CONTENT = "content";
    private TextView textView;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        textView = new TextView(this);
        textView.setPadding(10, 10, 10, 10);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setVerticalScrollBarEnabled(true);
        textView.setHorizontalScrollBarEnabled(true);
        textView.setHorizontallyScrolling(true);
        textView.setTextIsSelectable(true);
        setContentView(textView);
        new ZoomView<TextView>(textView){

            @Override
            protected void zoomIn() {
                textView.setTextSize(textView.getTextSize() / displayMetrics.density / 1.01f);
            }

            @Override
            protected void zoomOut() {
                textView.setTextSize(textView.getTextSize() / displayMetrics.density * 1.01f);
            }
        };
        String content = getIntent().getStringExtra(EXTRA_CONTENT);
        if (!TextUtils.isEmpty(content)) {
            try{
                String json = JsonFormat.format(content);
                textView.setText(json);
            } catch(Exception e) {
                e.printStackTrace();
                textView.setText("格式化出错：\n" + content);
            }
        } else {
            textView.setText("json字符串为空");
        }
        Toast.makeText(this, "支持手势缩放", Toast.LENGTH_SHORT).show();
    }

    /**
     * view缩放
     */
    public abstract class ZoomView<V extends View> {
        protected V view;
        private static final int NONE = 0;// 空
        private static final int DRAG = 1;// 按下第一个点
        private static final int ZOOM = 2;// 按下第二个点
        /** 屏幕上点的数量 */
        private int mode = NONE;
        /** 记录按下第二个点距第一个点的距离 */
        float oldDist;
        public ZoomView(V view) {
            this.view = view;
            setTouchListener();
        }
        private void setTouchListener() {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_DOWN:
                            mode = DRAG;
                            break;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_POINTER_UP:
                            mode = NONE;
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            oldDist = spacing(event);
                            if (oldDist > 10f) {
                                mode = ZOOM;
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (mode == ZOOM) {
                                // 正在移动的点距初始点的距离
                                float newDist = spacing(event);

                                if (newDist > oldDist) {
                                    zoomOut();
                                } else if (newDist < oldDist) {
                                    zoomIn();
                                }
                                oldDist = newDist;
                            }
                            break;
                    }
                    return false;
                }
                /**
                 * 求出2个触点间的 距离
                 */
                private float spacing(MotionEvent event) {
                    float x = event.getX(0) - event.getX(1);
                    float y = event.getY(0) - event.getY(1);

                    return (float) Math.sqrt(x * x + y * y);
                }
            });
        }
        protected abstract void zoomIn();
        protected abstract void zoomOut();
    }
}
