package com.jyuesong.dragview2fill.dragview;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by jiang on 29/01/2018.
 */
public class LayoutHelper {

    public static SparseArray<int[]> locations = new SparseArray<>();

    public static void layoutChild(ViewGroup parent, View child) {

        int tag = (int) child.getTag();

        if (tag == DragViewGroup.T_XUANZE) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int[] locaition = new int[4];
            locaition[0] = parent.getPaddingLeft() + lp.leftMargin;
            locaition[1] = parent.getPaddingTop() + lp.topMargin;
            locaition[2] = childWidth + parent.getPaddingLeft() + lp.leftMargin;
            locaition[3] = childHeight + parent.getPaddingTop() + lp.topMargin;
            child.layout(locaition[0], locaition[1], locaition[2], locaition[3]);
            locations.append(DragViewGroup.T_XUANZE, locaition);
        } else if (tag == DragViewGroup.T_PANDUAN) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int[] locaition = new int[4];
            locaition[0] = parent.getPaddingLeft() + lp.leftMargin;
            locaition[1] = parent.getPaddingTop() + lp.topMargin + locations.get(DragViewGroup.T_XUANZE)[3];
            locaition[2] = childWidth + parent.getPaddingLeft() + lp.leftMargin;
            locaition[3] = childHeight + locaition[1];
            child.layout(locaition[0], locaition[1], locaition[2], locaition[3]);
            locations.append(DragViewGroup.T_PANDUAN, locaition);
        } else {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            int[] locaition = new int[4];
            locaition[0] = parent.getPaddingLeft() + lp.leftMargin;
            locaition[1] = parent.getPaddingTop() + lp.topMargin + locations.get(DragViewGroup.T_PANDUAN)[3];
            locaition[2] = childWidth + parent.getPaddingLeft() + lp.leftMargin;
            locaition[3] = childHeight + locaition[1];
            child.layout(locaition[0], locaition[1], locaition[2], locaition[3]);
            locations.append(DragViewGroup.T_ZHUGUAN, locaition);
        }

    }

    public static void layoutRecyclerView(ViewGroup parent, View child) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childWidth = child.getMeasuredWidth();
        int childHeight = child.getMeasuredHeight();
        child.layout(parent.getWidth() / 3, parent.getPaddingTop() + mlp.topMargin,
                parent.getWidth() / 3 + childWidth, parent.getPaddingTop() + mlp.topMargin + childHeight);
    }
}
