package ca.six.ui.others.loading.mine1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by songzhw on 2016/4/4.
 */
public class LoadingView extends View {
    private Drawable drawable;
    private boolean isAnimating;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context ctx) {
        drawable = new FoldingCubeDrawable();
        drawable.setCallback(this);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        drawable.setBounds(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawable.draw(canvas);
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
            return drawable == who;
        }
        return super.verifyDrawable(who);
    }

    // ====================== Public Method ================================


}
