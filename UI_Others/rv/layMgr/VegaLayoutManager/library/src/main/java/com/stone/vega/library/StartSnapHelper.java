package com.stone.vega.library;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 定位到第一个子View的SnapHelper
 * Created by xmuSistone on 2017/9/19.
 */
public class StartSnapHelper extends LinearSnapHelper {

    // 告知SnapHelper, 哪个才是snapView, 这样才好计算这个snapView要滑动多少才能到我们期望的位置(如最左, 或居中, 或...)
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        VegaLayoutManager custLayoutManager = (VegaLayoutManager) layoutManager;
        return custLayoutManager.findSnapView();
    }


    // 返回值, 会让rv.smoothScrollby(out[0], out[1]. -- 应是松手后的动画
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];
        out[0] = 0;
        out[1] = ((VegaLayoutManager) layoutManager).getSnapHeight();
        return out;
    }

}