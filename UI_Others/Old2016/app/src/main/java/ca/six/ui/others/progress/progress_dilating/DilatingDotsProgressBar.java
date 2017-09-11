package ca.six.ui.others.progress.progress_dilating;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import ca.six.ui.others.R;

public class DilatingDotsProgressBar extends View {
    public static final double START_DELAY_FACTOR = 0.35;
    private static final float DEFAULT_GROWTH_MULTIPLIER = 1.75f;
    private static final int MIN_SHOW_TIME = 500; // ms
    private static final int MIN_DELAY = 500; // ms

    // widthBetweenDotCenters =  (dotRadius * 2) +  horizontalSpacing;
    private int dotColor, animDuration, widthBetweenDotCenters, numberDots;
    private float dotRadius, dotScaleMultiplier, dotMaxRadius, horizontalSpacing;
    private long startTime = -1;
    private boolean shouldAnimate, isDismissed = false;

    private ArrayList<DilatingDotDrawable> drawables = new ArrayList<>();
    private final List<Animator> animators = new ArrayList<>();



    public DilatingDotsProgressBar(Context context) {
        this(context, null);
    }

    public DilatingDotsProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DilatingDotsProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DilatingDotsProgressBar);
        numberDots = a.getInt(R.styleable.DilatingDotsProgressBar_dd_numDots, 3);
        dotRadius = a.getDimension(R.styleable.DilatingDotsProgressBar_android_radius, 8);
        dotColor = a.getColor(R.styleable.DilatingDotsProgressBar_android_color, 0xff9c27b0);
        dotScaleMultiplier = a.getFloat(R.styleable.DilatingDotsProgressBar_dd_scaleMultiplier, DEFAULT_GROWTH_MULTIPLIER);// 1.75f
        animDuration = a.getInt(R.styleable.DilatingDotsProgressBar_dd_animationDuration, 300);
        horizontalSpacing = a.getDimension(R.styleable.DilatingDotsProgressBar_dd_horizontalSpacing, 12);
        a.recycle();

        shouldAnimate = false;
        calculateMaxRadius();
        calculateWidthBetweenDotCenters();

        initDots();
        updateDots();
    }

    private void calculateMaxRadius() {
        dotMaxRadius = dotRadius * dotScaleMultiplier;
    }

    private void calculateWidthBetweenDotCenters() {
        widthBetweenDotCenters = (int) (dotRadius * 2) + (int) horizontalSpacing;
    }


    private void initDots() {
        drawables.clear();
        animators.clear();
        for (int i = 1; i <= numberDots; i++) {
            final DilatingDotDrawable dot = new DilatingDotDrawable(dotColor, dotRadius, dotMaxRadius);
            dot.setCallback(this);
            drawables.add(dot);

            ValueAnimator growAnimator = ObjectAnimator.ofFloat(dot, "radius", dotRadius, dotMaxRadius, dotRadius);
            growAnimator.setDuration(animDuration);
            growAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

            if (i == numberDots) {
                growAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (shouldAnimate()) {
                            startAnimations();
                        }
                    }
                });
            }

            growAnimator.setStartDelay((i - 1) * (int) (START_DELAY_FACTOR * animDuration)); // 0.35 * animDuration
            animators.add(growAnimator);
        }
    }

    private void updateDots() {
        if (dotRadius <= 0) {
            dotRadius = getHeight() / 2 / dotScaleMultiplier;
        }

        int left = (int) (dotMaxRadius - dotRadius); // 即小圆形态时的left
        int right = (int) (left + dotRadius * 2) + 2; // 也是小圆形态下的right (= left + 2*radius)
        int top = 0;
        int bottom = (int) (dotMaxRadius * 2) + 2;

        for (int i = 0; i < drawables.size(); i++) {
            final DilatingDotDrawable dot = drawables.get(i);
            dot.setRadius(dotRadius);
            dot.setBounds(left, top, right, bottom);
            ValueAnimator growAnimator = (ValueAnimator) animators.get(i);
            growAnimator.setFloatValues(dotRadius, dotRadius * dotScaleMultiplier, dotRadius); // small -> big -> small again

            left += widthBetweenDotCenters;
            right += widthBetweenDotCenters;
        }
    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((int) computeMaxWidth(), (int) computeMaxHeight());
    }

    private float computeMaxHeight() {
        return dotMaxRadius * 2;
    }

    private float computeMaxWidth() {
        return computeWidth() + ((dotMaxRadius - dotRadius) * 2);
    }

    private float computeWidth() {
        return (((dotRadius * 2) + horizontalSpacing) * drawables.size()) - horizontalSpacing;
    }



    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (computeMaxHeight() != h || w != computeMaxWidth()) {
            updateDots();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (shouldAnimate()) {
            for (DilatingDotDrawable dot : drawables) {
                dot.draw(canvas);
            }
        }
    }

    @Override
    protected boolean verifyDrawable(final Drawable who) {
        if (shouldAnimate()) {
            return drawables.contains(who);
        }
        return super.verifyDrawable(who); // View # verifyDrawable(drawable)
        /*
        View # verifyDrawable(drawable)

           If your view subclass is displaying its own Drawable objects, it should override this function and return true for any Drawable it is displaying.
           This allows animations for those drawables to be scheduled.

           Be sure to call through to the super class when overriding this function.
        */
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks();
    }

    private void removeCallbacks() {
        removeCallbacks(delayedHide);
        removeCallbacks(delayedShow);
    }




    /**
     * Show the progress view after waiting for a minimum delay. If
     * during that time, hide() is called, the view is never made visible.
     */
    @SuppressWarnings ("unused")
    public void show() {
        show(MIN_DELAY);
    }

    @SuppressWarnings ("unused")
    public void showNow() {
        show(0);
    }


    public void show(int delay) {
        startTime = -1;
        isDismissed = false;
        removeCallbacks(delayedHide);

        if (delay == 0) {
            delayedShow.run();
        } else {
            postDelayed(delayedShow, delay);
        }
    }

    /** delayed runnable to start the progress */
    private final Runnable delayedShow = new Runnable() {
        @Override
        public void run() {
            if (!isDismissed) {
                startTime = System.currentTimeMillis();
                setVisibility(View.VISIBLE);
                startAnimations();
            }
        }
    };

    protected void startAnimations() {
        shouldAnimate = true;
        for (Animator anim : animators) {
            anim.start();
        }
    }




    protected void stopAnimations() {
        shouldAnimate = false;
        removeCallbacks();
        for (Animator anim : animators) {
            anim.cancel();
        }
    }

    protected boolean shouldAnimate() {
        return shouldAnimate;
    }


    public void reset() {
        hideNow();
    }

    /**
     * Hide the progress view if it is visible. The progress view will not be
     * hidden until it has been shown for at least a minimum show time. If the
     * progress view was not yet visible, cancels showing the progress view.
     */
    @SuppressWarnings ("unused")
    public void hide() {
        hide(MIN_SHOW_TIME);
    }


    @SuppressWarnings ("unused")
    public void hideNow() {
        hide(0);
    }


    public void hide(int delay) {
        isDismissed = true;
        removeCallbacks(delayedShow);
        long diff = System.currentTimeMillis() - startTime;
        if ((diff >= delay) || (startTime == -1)) {
            delayedHide.run();
        } else {
            if ((delay - diff) <= 0) {
                delayedHide.run();
            } else {
                postDelayed(delayedHide, delay - diff);
            }
        }
    }


    /** delayed runnable to stop the progress */
    private final Runnable delayedHide = new Runnable() {
        @Override
        public void run() {
            startTime = -1;
            setVisibility(View.GONE);
            stopAnimations();
        }
    };


    // -------------------------------
    // ------ Getters & Setters ------
    // -------------------------------

    public void setDotRadius(float radius) {
        reset();
        dotRadius = radius;
        calculateMaxRadius();
        calculateWidthBetweenDotCenters();
        setupDots();
    }

    public void setDotSpacing(float horizontalSpacing) {
        reset();
        this.horizontalSpacing = horizontalSpacing;
        calculateWidthBetweenDotCenters();
        setupDots();
    }

    public void setGrowthSpeed(int growthSpeed) {
        reset();
        animDuration = growthSpeed;
        setupDots();
    }

    public void setNumberOfDots(int numDots) {
        reset();
        numberDots = numDots;
        setupDots();
    }

    public void setDotScaleMultpiplier(float multplier) {
        reset();
        dotScaleMultiplier = multplier;
        calculateMaxRadius();
        setupDots();
    }

    public void setDotColor(int color) {
        dotColor = color;
        for (DilatingDotDrawable dot : drawables) {
            dot.setColor(dotColor);
        }
    }

    private void setupDots() {
        initDots();
        updateDots();
        showNow();
    }

    public int getDotGrowthSpeed() {
        return animDuration;
    }

    public float getDotRadius() {
        return dotRadius;
    }

    public float getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public int getNumberOfDots() {
        return numberDots;
    }

    public float getDotScaleMultiplier() {
        return dotScaleMultiplier;
    }
}
