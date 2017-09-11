package ca.six.ui.others.swipedeck.split;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

/**
 * Created by songzhw on 2016/1/11
 */
public class MySwipeDeck extends FrameLayout {

    private float startX, startY;
    private float childInitX, childInitY;
    private View topChild;
    private BaseAdapter adapter;

    public MySwipeDeck(Context context) {
        this(context, null);
    }

    public MySwipeDeck(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // onMeasure() --> onMeasure() --> onSizeChanged() --> onLayout()o
    //  --> onMeasure() --> onLayout()
    //  : It's the "onSizeChanged()" only be called once!

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        System.out.println("szw onSizeChanged()");
        initChild();
    }

    private void initChild() {
        // in this places, the adapter is already not null !
        if(adapter == null || adapter.getCount() == 0 ){
            removeAllViewsInLayout();
            return;
        }

        int childCount = adapter.getCount();
        for(int i = 0 ; i < childCount ; i++){
            View child = adapter.getView(i, null/*last removed view*/, this);

            ViewGroup.LayoutParams params = child.getLayoutParams();
            if (params == null) {
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            addViewInLayout(child, -1, params, true); // the default value of the forth argument is "false"
        }

        if(this.getChildCount() > 0) {
            this.topChild = getChildAt(0);
        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(adapter == null || adapter.getCount() == 0 ){
            removeAllViewsInLayout();
            return;
        }

        System.out.println("szw onLayout()");
        int childCount = this.getChildCount();
        View aChild = this.getChildAt(0);
        if(aChild == null){
            return;
        }
        int childWidth = aChild.getMeasuredWidth();
        int childheight = aChild.getMeasuredHeight();
        int left2 = (getWidth() - childWidth)/2 - 50;
        int top2 = (getHeight() - childheight)/2 - 50;
        for(int i = 0 ; i < childCount ; i++){
            View child = this.getChildAt(i);
            ViewCompat.setTranslationZ(child, 10 - i);

            int step = i * 25;
            child.layout(left2 + step, top2 + step, left2 + childWidth + step, top2 + childheight + step);
        }

    }

    // onTouchEvent() : not triggered. Because every child has their own click event.
    // dispatchTouchEvent() : it is okay.


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                this.startX = ev.getX();
                this.startY = ev.getY();
                childInitX = topChild.getX();
                childInitY = topChild.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float moveY = ev.getY();
                float dx = moveX - startX;
                float dy = moveY - startY;

                float newX = childInitX + dx;
                float newY = childInitY + dy;
                topChild.setX(newX);
                topChild.setY(newY);

                float rotation = 30 * newX / this.getMeasuredWidth();
                topChild.setRotation(rotation);
                break;

            case MotionEvent.ACTION_UP:
                this.removeView(topChild); // system will call "onLayout()" again
                if(this.getChildCount() > 0) {
                    this.topChild = getChildAt(0);
                }
                break;
        }

        return true;
    }

    // ============== Public Methods ==============

    public void refresh(){
        this.initChild();
        this.requestLayout();
    }

    public void setAdapter(BaseAdapter adapter){
        this.adapter = adapter;
    }



}
