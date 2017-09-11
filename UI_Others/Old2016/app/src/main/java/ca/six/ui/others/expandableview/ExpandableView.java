package ca.six.ui.others.expandableview;

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

import java.util.ArrayList;
import java.util.List;

import ca.six.ui.others.R;

/**
 * ExpandableView is a View showing only a visible content and when clicked on it, it displays more content in a fashion way.
 * It can also handle inner ExpandableViews, one ExpandableView inside another and another.
 */
public class ExpandableView extends RelativeLayout {

    private static final int DURATION = 400;
    private int childContentHeight = 0;
    private float DEGREES;

    private RelativeLayout rlayTitle;
    private LinearLayout llayChildren;
    private TextView tvTitle;
    private ImageView ivTitle, ivRight;

    private List<ViewGroup> children;

    private ValueAnimator animator;
    private RotateAnimation rotateAnimator;

    public ExpandableView(Context context) {
        super(context);
        init();
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.expandable_view, this);

        children = new ArrayList<>();

        tvTitle = (TextView) findViewById(R.id.expandable_view_title);
        rlayTitle = (RelativeLayout) findViewById(R.id.expandable_view_clickable_content);
        llayChildren = (LinearLayout) findViewById(R.id.expandable_view_content_layout);
        ivTitle = (ImageView) findViewById(R.id.expandable_view_image);
        ivRight = (ImageView) findViewById(R.id.expandable_view_right_icon);

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


        //Add onPreDrawListener
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

    public void setOutsideContentLayout(ViewGroup outsideContentLayout) {
        this.children.add(outsideContentLayout);
    }

    public void setOutsideContentLayout(ViewGroup... params) {
        for (int i = 0; i < params.length; i++) {
            this.children.add(params[i]);
        }
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

    public void fillData(int leftResId, String text, boolean useChevron) {
        tvTitle.setText(text);

        if (leftResId == 0) {
            ivTitle.setVisibility(GONE);
        } else {
            ivTitle.setImageResource(leftResId);
        }

        if (useChevron) {
            DEGREES = 180;
            ivRight.setImageResource(R.drawable.ic_expandable_view_chevron);
        } else {
            DEGREES = -225;
            ivRight.setImageResource(R.drawable.ic_expandable_view_plus);
        }
    }

    public void fillData(int leftResId, int stringResId, boolean useChevron) {
        fillData(leftResId, getResources().getString(stringResId), useChevron);
    }

    public void fillData(int leftResId, String text) {
        fillData(leftResId, text, false);
    }

    public void fillData(int leftResId, int stringResId) {
        fillData(leftResId, getResources().getString(stringResId), false);
    }


    public void expand() {
        //set Visible
        llayChildren.setVisibility(View.VISIBLE);

        int x = ivRight.getMeasuredWidth() / 2;
        int y = ivRight.getMeasuredHeight() / 2;
        rotateAnimator = new RotateAnimation(0f, DEGREES, x, y);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
        rotateAnimator.setFillAfter(true);
        rotateAnimator.setDuration(DURATION);
        ivRight.startAnimation(rotateAnimator);

        animator.start();
    }

    /**
     * Collapse animation to hide the discoverable content.
     */
    public void collapse() {
        int finalHeight = llayChildren.getHeight();

        int x = ivRight.getMeasuredWidth() / 2;
        int y = ivRight.getMeasuredHeight() / 2;
        rotateAnimator = new RotateAnimation(DEGREES, 0f, x, y);
        rotateAnimator.setInterpolator(new LinearInterpolator());
        rotateAnimator.setRepeatCount(Animation.ABSOLUTE);
        rotateAnimator.setFillAfter(true);
        rotateAnimator.setDuration(DURATION);
        ivRight.startAnimation(rotateAnimator);

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

                if (children != null && !children.isEmpty()) {
                    for (ViewGroup aChild : children) {
                        ViewGroup.LayoutParams aCildLayoutParams = aChild.getLayoutParams();

                        if (childContentHeight == 0) {
                            childContentHeight = aCildLayoutParams.height;
                        }

                        aCildLayoutParams.height = childContentHeight + value;
                        aChild.setLayoutParams(aCildLayoutParams);
                        aChild.invalidate();
                    }
                }
            }
        });
        return animator;
    }

}
