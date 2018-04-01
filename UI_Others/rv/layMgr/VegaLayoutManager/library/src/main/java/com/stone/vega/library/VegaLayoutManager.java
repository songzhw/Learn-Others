package com.stone.vega.library;

import android.graphics.Rect;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xmuSistone on 2017/9/20.
 */
public class VegaLayoutManager extends RecyclerView.LayoutManager {
    private SparseArray<Rect> locationRects = new SparseArray<>();
    private SparseBooleanArray isItemAttachedArray = new SparseBooleanArray();
    private ArrayMap<Integer, Integer> viewType2HeightMap = new ArrayMap<>(); //viewType2HeightMap.put(viewType, itemHeight);
    private boolean isDragging = false;

    private int scroll = 0;
    private int lastDy = 0;
    private int maxScroll = -1;

    private RecyclerView.Adapter adapter; //要由此adapter来得到viewType. 本demo中只有一个viewType, 但难保其它adapter场景会没有多个ViewType
    private RecyclerView.Recycler recycler;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        super.onAdapterChanged(oldAdapter, newAdapter);
        this.adapter = newAdapter;
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        new StartSnapHelper().attachToRecyclerView(view);
    }

    // 返回true, 就是用rv的measure. 要么就返回false, 然后自己重写LayoutManager.onMeasure()方法
    @Override
    public boolean isAutoMeasureEnabled() {
        return true;
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        this.recycler = recycler;

        if (state.isPreLayout()) {
            return;
        }

        buildLocationRects();
        this.detachAndScrapAttachedViews(recycler); // 先回收放到缓存，后面会再次统一layout. LayoutManager的方法
        layoutItemsOnCreate(recycler);
    }

    private void buildLocationRects() {
        locationRects.clear();
        isItemAttachedArray.clear();

        int currentTop = getPaddingTop();
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            // 1. 先计算出itemWidth和itemHeight
            int viewType = adapter.getItemViewType(i);
            int itemHeight;
            if (viewType2HeightMap.containsKey(viewType)) {
                itemHeight = viewType2HeightMap.get(viewType);
            } else {
                View itemView = recycler.getViewForPosition(i);
                addView(itemView);
                measureChildWithMargins(itemView, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED); //unspecfied值为0
                itemHeight = getDecoratedMeasuredHeight(itemView); //这是LayoutManager的方法. 其实就是child.getMeasuredHeight() + decoInsets.top + decoInsets.bottom;
                viewType2HeightMap.put(viewType, itemHeight);
            }

            // 2. 组装Rect并保存
            Rect rect = new Rect();
            rect.left = getPaddingLeft();
            rect.right = getWidth() - getPaddingRight();
            rect.top = currentTop;
            rect.bottom = rect.top + itemHeight;
            locationRects.put(i, rect);
            isItemAttachedArray.put(i, false); //先全置false. 后面layotuOnCreate()时会把可见项的值设为true
            currentTop = currentTop + itemHeight;
        }

        if (itemCount == 0) {
            maxScroll = 0;
        } else {
            computeMaxScroll();
        }
    }


    /**
     * 计算可滑动的最大值
     */
    private void computeMaxScroll() {
        // 这一点和"监听Scroll是否滑动到底了"十分类似
        maxScroll = locationRects.get(locationRects.size() - 1).bottom - getHeight(); //getHeight()一般来说就是rv.height
        if (maxScroll < 0) {
            maxScroll = 0;
            return;
        }

        int itemCount = getItemCount();
        int screenFilledHeight = 0;
        for (int i = itemCount - 1; i >= 0; i--) {
            Rect rect = locationRects.get(i);
            int thisItemHeight = rect.bottom - rect.top;
            screenFilledHeight = screenFilledHeight + thisItemHeight;
            // 若一进来就最多显示了10项, 第10项只能显示一部分了. 这时screenFilledHeight就大于getHeight(),
            // 这时screenFillHeight - thisItemHeight, 其实就是前9项的items的高度和. extraSnapHeight就成了第10项显示了多少个像素.
            if (screenFilledHeight > getHeight()) {
                int extraSnapHeight = getHeight() - (screenFilledHeight - thisItemHeight);
                maxScroll = maxScroll + extraSnapHeight;
                break;
            }
        }
    }

    /**
     * 初始化的时候，layout子View
     */
    private void layoutItemsOnCreate(RecyclerView.Recycler recycler) {
        int itemCount = getItemCount();
        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll); //scroll一开始为0
        for (int i = 0; i < itemCount; i++) {
            Rect thisRect = locationRects.get(i);
            if (Rect.intersects(displayRect, thisRect)) { //即可见, 至少是部分可见.
                View childView = recycler.getViewForPosition(i);
                this.addView(childView);
                this.measureChildWithMargins(childView, View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                layoutMyChildren(childView, locationRects.get(i));
                isItemAttachedArray.put(i, true);
                childView.setPivotY(0);
                childView.setPivotX(childView.getMeasuredWidth() / 2);
                if (thisRect.top - scroll > getHeight()) {
                    break;
                }
            }
        }
    }



    private void layoutMyChildren(View child, Rect rect) {
        int topDistance = scroll - rect.top;
        int layoutTop, layoutBottom;
        int itemHeight = rect.bottom - rect.top;

        //要滑出去的部分, 要修改它们的scaleX, scaleY, alpha值
        if (topDistance < itemHeight && topDistance > 0) {
            float rate1 = (float) topDistance / itemHeight;
            float rate2 = 1 - rate1 * rate1 / 3;
            float rate3 = 1 - rate1 * rate1;
            child.setScaleX(rate2);
            child.setScaleY(rate2);
            child.setAlpha(rate3);
            layoutTop = 0;
            layoutBottom = itemHeight;
        } else {
            child.setScaleX(1);
            child.setScaleY(1);
            child.setAlpha(1);

            layoutTop = rect.top - scroll;
            layoutBottom = rect.bottom - scroll;
        }
        this.layoutDecorated(child, rect.left, layoutTop, rect.right, layoutBottom);
    }



    // =========================== Scroll Vertically ===========================

    @Override
    public void onScrollStateChanged(int state) {
        if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
            isDragging = true;
        }
        super.onScrollStateChanged(state);
    }


    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0 || dy == 0) {
            return 0;
        }
        int travel = dy;
        if (dy + scroll < 0) {
            travel = -scroll;
        } else if (dy + scroll > maxScroll) {
            travel = maxScroll - scroll;
        }
        scroll += travel; //累计偏移量
        lastDy = dy;
        if (!state.isPreLayout() && getChildCount() > 0) {
            layoutItemsOnScroll();
        }

        return travel;
    }


    /**
     * 初始化的时候，layout子View
     */
    private void layoutItemsOnScroll() {
        int childCount = getChildCount();
        // 1. 已经在屏幕上显示的child
        int itemCount = getItemCount();
        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        int firstVisiblePosition = -1;
        int lastVisiblePosition = -1;
        for (int i = childCount - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child == null) {
                continue;
            }
            int position = getPosition(child);
            if (!Rect.intersects(displayRect, locationRects.get(position))) {
                // 回收滑出屏幕的View
                removeAndRecycleView(child, recycler);
                isItemAttachedArray.put(position, false);
            } else {
                // Item还在显示区域内，更新滑动后Item的位置
                if (lastVisiblePosition < 0) {
                    lastVisiblePosition = position;
                }

                if (firstVisiblePosition < 0) {
                    firstVisiblePosition = position;
                } else {
                    firstVisiblePosition = Math.min(firstVisiblePosition, position);
                }

                layoutMyChildren(child, locationRects.get(position)); //更新Item位置
            }
        }

        // 2. 复用View处理
        if (firstVisiblePosition > 0) {
            // 往前搜索复用
            for (int i = firstVisiblePosition - 1; i >= 0; i--) {
                if (Rect.intersects(displayRect, locationRects.get(i)) &&
                        !isItemAttachedArray.get(i)) {
                    reuseItemOnSroll(i, true);
                } else {
                    break;
                }
            }
        }
        // 往后搜索复用
        for (int i = lastVisiblePosition + 1; i < itemCount; i++) {
            if (Rect.intersects(displayRect, locationRects.get(i)) &&
                    !isItemAttachedArray.get(i)) {
                reuseItemOnSroll(i, false);
            } else {
                break;
            }
        }
    }


    /**
     * 复用position对应的View
     */
    private void reuseItemOnSroll(int position, boolean addViewFromTop) {
        View scrap = recycler.getViewForPosition(position);
        measureChildWithMargins(scrap, 0, 0);
        scrap.setPivotY(0);
        scrap.setPivotX(scrap.getMeasuredWidth() / 2);

        if (addViewFromTop) {
            addView(scrap, 0);
        } else {
            addView(scrap);
        }
        // 将这个Item布局出来
        layoutMyChildren(scrap, locationRects.get(position));
        isItemAttachedArray.put(position, true);
    }


    // =========================== 对外提供的接口 ===========================

    public int findFirstVisibleItemPosition() {
        int count = locationRects.size();
        Rect displayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        for (int i = 0; i < count; i++) {
            if (Rect.intersects(displayRect, locationRects.get(i)) &&
                    isItemAttachedArray.get(i)) {
                return i;
            }
        }
        return 0;
    }

    public int getSnapHeight() {
        if (!isDragging) {
            return 0;
        }
        isDragging = false;

        Rect rvDisplayRect = new Rect(0, scroll, getWidth(), getHeight() + scroll);
        int itemCount = getItemCount();
        for (int i = 0; i < itemCount; i++) {
            Rect itemRect = locationRects.get(i);
            if (rvDisplayRect.intersect(itemRect)) {
                if (lastDy > 0) {
                    // scroll变大，属于列表往下走，往下找下一个为snapView
                    if (i < itemCount - 1) {
                        Rect nextRect = locationRects.get(i + 1);
                        return nextRect.top - rvDisplayRect.top;
                    }
                }
                return itemRect.top - rvDisplayRect.top;
            }
        }
        return 0;
    }

    // 总是第0项, 才有可能有我们的snap的效果
    public View findSnapView() {
        if (getChildCount() > 0) {
            return getChildAt(0);
        }
        return null;
    }

}
/*
1. 没处理fling?
2. 没用offsetChildrenVertically(), 而是自己layout when scrolling


 */

