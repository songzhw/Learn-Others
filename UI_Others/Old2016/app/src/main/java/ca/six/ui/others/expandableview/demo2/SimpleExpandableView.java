package ca.six.ui.others.expandableview.demo2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ca.six.ui.others.R;

public class SimpleExpandableView extends RelativeLayout {

    private static final int DURATION = 400;
    private float DEGREES;

    private RelativeLayout rlayTitle;
    private LinearLayout llayChildren;
    private TextView tvTitle;
    private ImageView ivTitle, ivExpand;

    private ValueAnimator animator;
    private RotateAnimation rotateAnimator;

    public SimpleExpandableView(Context context) {
        super(context);
        init();
    }

    public SimpleExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.expandable_view, this);

        rlayTitle = (RelativeLayout) findViewById(R.id.expandable_view_clickable_content);
        llayChildren = (LinearLayout) findViewById(R.id.expandable_view_content_layout);
        tvTitle = (TextView) findViewById(R.id.expandable_view_title);
        ivTitle = (ImageView) findViewById(R.id.expandable_view_image);
        ivExpand = (ImageView) findViewById(R.id.expandable_view_right_icon);

        llayChildren.setVisibility(GONE);
        rlayTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llayChildren.isShown()) {
                    collapse();
                } else {
                    expand();
                }
            }
        });


        //Add onPreDrawListener (orders : fillData() --> addContentView() --> onPreDrawListener)
        llayChildren.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        llayChildren.getViewTreeObserver().removeOnPreDrawListener(this);
                        llayChildren.setVisibility(View.GONE);

                        final int widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                        final int heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
                        llayChildren.measure(widthSpec, heightSpec);

                        animator = slideAnimator(0, llayChildren.getMeasuredHeight());
                        return true;
                    }
                });
    }

    public void setVisibleLayoutHeight(int newHeight) {
        this.rlayTitle.getLayoutParams().height = newHeight;
    }

    public TextView getTvTitle() {
        return this.tvTitle;
    }

    public LinearLayout getLlayChildren() {
        return this.llayChildren;
    }

    public void addContentView(View newContentView) {
        llayChildren.addView(newContentView);
        llayChildren.invalidate();
    }

    public void fillData(int leftImgResId, String text, boolean useChevron) {
        tvTitle.setText(text);

        if (leftImgResId == 0) {
            ivTitle.setVisibility(GONE);
        } else {
            ivTitle.setImageResource(leftImgResId);
        }

        if (useChevron) {
            DEGREES = 180;
            ivExpand.setImageResource(R.drawable.ic_expandable_view_chevron);
        } else {
            DEGREES = -225;
            ivExpand.setImageResource(R.drawable.ic_expandable_view_plus);
        }
    }

    public void fillData(int leftResId, int stringResId, boolean useChevron) {
        fillData(leftResId, getResources().getString(stringResId), useChevron);
    }

    public void fillData(int leftImgResId, String text) {
        fillData(leftImgResId, text, false);
    }

    public void fillData(int leftImgResId, int stringResId) {
        fillData(leftImgResId, getResources().getString(stringResId), false);
    }


    public void expand() {
        //set Visible
        llayChildren.setVisibility(View.VISIBLE);

        int x = ivExpand.getMeasuredWidth() / 2;
        int y = ivExpand.getMeasuredHeight() / 2;
        rotateAnimator = new RotateAnimation(0f, DEGREES, x, y);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
        rotateAnimator.setFillAfter(true);
        rotateAnimator.setDuration(DURATION);
        ivExpand.startAnimation(rotateAnimator);

        animator.start();
    }

    public void collapse() {
        int finalHeight = llayChildren.getHeight();

        int x = ivExpand.getMeasuredWidth() / 2;
        int y = ivExpand.getMeasuredHeight() / 2;
        rotateAnimator = new RotateAnimation(DEGREES, 0f, x, y);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
        rotateAnimator.setFillAfter(true);
        rotateAnimator.setDuration(DURATION);
        ivExpand.startAnimation(rotateAnimator);

        ValueAnimator collapseAnim = slideAnimator(finalHeight, 0);
        collapseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                llayChildren.setVisibility(View.GONE);
            }
        });
        collapseAnim.start();
    }


    private ValueAnimator slideAnimator(int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(DURATION);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = llayChildren.getLayoutParams();
                layoutParams.height = value;

                llayChildren.setLayoutParams(layoutParams);
                llayChildren.invalidate();

            }
        });
        return animator;
    }

}