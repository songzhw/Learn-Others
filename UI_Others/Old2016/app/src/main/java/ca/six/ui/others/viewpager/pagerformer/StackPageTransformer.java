package ca.six.ui.others.viewpager.pagerformer;

import android.support.v4.view.ViewPager;
import android.view.View;

public class StackPageTransformer implements ViewPager.PageTransformer {
    private float minScale;    // 栈底: 最小页面缩放比
    private float maxScale;    // 栈顶: 最大页面缩放比
    private int stackCount;    // 栈内页面数
    private float powBase;     // 基底: 相邻两 page 的大小比例

    /**
     * @param minScale   栈底: 最小页面缩放比
     * @param maxScale   栈顶: 最大页面缩放比
     * @param stackCount 栈内页面数
     */
    public StackPageTransformer(ViewPager viewPager, float minScale, float maxScale, int stackCount) {
        viewPager.setOffscreenPageLimit(stackCount);
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.stackCount = stackCount;

        if (this.maxScale < this.minScale) {
            throw new IllegalArgumentException("The Argument: maxScale must bigger than minScale !");
        }
        powBase = (float) Math.pow(this.minScale / this.maxScale, 1.0f / this.stackCount);
    }

    public StackPageTransformer(ViewPager viewPager) {
        this(viewPager, 0.8f, 0.9f, 5);
    }

    public final void transformPage(View view, float position) {

        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        view.setPivotX(pageWidth / 2);
        view.setPivotY(0);

        float bottomPos = stackCount - 1;

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);

            view.setTranslationX(0);
            view.setScaleX(maxScale);
            view.setScaleY(maxScale);

            if (!view.isClickable())
                view.setClickable(true);

        } else if (position <= bottomPos) { // (0, stackCount-1]
            int index = (int) position;  // 整数部分
            float minScale = maxScale * (float) Math.pow(powBase, index + 1);
            float maxScale = this.maxScale * (float) Math.pow(powBase, index);
            float curScale = this.maxScale * (float) Math.pow(powBase, position);

            // Fade the page out.
            view.setAlpha(1);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);
            view.setTranslationY(pageHeight * (1 - curScale)
                    - pageHeight * (1 - this.maxScale) * (bottomPos - position) / bottomPos);

            // Scale the page down (between minScale and maxScale)
            float scaleFactor = minScale
                    + (maxScale - minScale) * (1 - Math.abs(position - index));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            if (position == 1 && view.isClickable())
                view.setClickable(false);

        } else { // (stackCount-1, +Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }
}  