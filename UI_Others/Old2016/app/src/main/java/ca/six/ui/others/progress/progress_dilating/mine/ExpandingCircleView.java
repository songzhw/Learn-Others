package ca.six.ui.others.progress.progress_dilating.mine;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songzhw on 2016/2/13.
 */
public class ExpandingCircleView extends View {
    private int numbers = 3, spacing = 40;
    private float radius = 40f, maxRadius = radius * 1.7f;
    private List<ExpandingCircleDrawable> drawables;
    private List<Animator> animators;
    private boolean isAnimating;

    public ExpandingCircleView(Context context) {
        super(context);
        init(context, null);
    }

    public ExpandingCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            // get custom attribute from TypedArray.
            // Then typedArray.recycle()
        }

        // TODO -- delete
        this.setBackgroundColor(Color.GRAY);

        drawables = new ArrayList<>(numbers);
        animators = new ArrayList<>(numbers);
        for(int i = 0 ; i < numbers ; i++){
            ExpandingCircleDrawable drawable = new ExpandingCircleDrawable(radius, maxRadius);
            drawable.setCallback(this);
            drawables.add(drawable);

            Animator anim = ObjectAnimator.ofFloat(drawable, "radius", radius, maxRadius, radius);
            anim.setDuration(700);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setStartDelay((long) (i * 0.5 * 700));
            animators.add(anim);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = (int) ((numbers * 2) * maxRadius + (numbers - 1) * spacing);
        width += (spacing + spacing); // two spacing in the head and tail.
        setMeasuredDimension( width,
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int singleLeft = left+spacing;
        int singleRight = (int) (singleLeft + maxRadius + maxRadius);
        for(Drawable d : drawables){
            d.setBounds(singleLeft, top, singleRight, bottom);
            singleLeft += (2 * maxRadius + spacing);
            singleRight += (2 * maxRadius + spacing); // 平移同样的距离
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(Drawable d : drawables){
            d.draw(canvas);
        }
    }
    // ====================== Detail Method ================================


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.isAnimating = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isAnimating = false;
    }

    /*
        View # verifyDrawable(drawable)

           If your view subclass is displaying its own Drawable objects, it should override this function and return true for any Drawable it is displaying.
           This allows animations for those drawables to be scheduled.

           Be sure to call through to the super class when overriding this function.
         */
    @Override
    protected boolean verifyDrawable(Drawable who) {
        if(isAnimating){
            return drawables.contains(who);
        }
        return super.verifyDrawable(who);
    }

    // ====================== Public Method ================================
    public void show(){
        for(Animator anim : animators){
            anim.start();
        }
    }
}
