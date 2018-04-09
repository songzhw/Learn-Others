package ca.six.lib;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


public class VegaLayoutManager extends RecyclerView.LayoutManager {

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    // 返回true, 就是用rv的measure. 要么就返回false, 然后自己重写LayoutManager.onMeasure()方法
    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }


    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        new VegaSnapHelper().attachToRecyclerView(view);
    }

    // ===================== static layout =====================
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }

    // ===================== Scroll =====================
    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy(dy, recycler, state);
    }


    // ===================== public methods =====================
    public int getSnapHeight(){
        return 0;
    }
}

/*
*
* adapter viewType
 */