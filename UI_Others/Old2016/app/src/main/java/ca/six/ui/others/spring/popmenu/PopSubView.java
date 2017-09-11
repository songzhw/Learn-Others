package ca.six.ui.others.spring.popmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PopSubView extends LinearLayout {

    private static final float factor = 1.2f;

    private ImageView ivIcon;
    private TextView tvTitle;

    public PopSubView(Context context) {
        this(context, null);
    }

    public PopSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        ivIcon = new ImageView(context);
        ivIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        addView(ivIcon, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tvTitle = new TextView(context);
        LayoutParams tvLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvLp.topMargin = 10;
        addView(tvTitle, tvLp);

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        scaleViewAnimation(PopSubView.this, factor);
                        break;
                    case MotionEvent.ACTION_UP:
                        scaleViewAnimation(PopSubView.this, 1);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 赋值
     *
     * @param popMenuItem
     */
    public void setPopMenuItem(PopMenuItem popMenuItem) {
        if (popMenuItem == null) return;
        ivIcon.setImageDrawable(popMenuItem.getDrawable());
        tvTitle.setText(popMenuItem.getTitle());
    }

    /**
     * 缩放动画
     *
     * @param value
     */
    private void scaleViewAnimation(View view, float value) {
        view.animate().scaleX(value).scaleY(value).setDuration(80).start();
    }
}
