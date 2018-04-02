package ca.six.lib;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class VegaSnapHelper extends LinearSnapHelper {
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof VegaLayoutManager) {
            if (layoutManager.getChildCount() > 0) {
                return layoutManager.getChildAt(0);
            }
        } else {
            throw new IllegalStateException("VegaSnapHelper can only work with VegaLayoutManager");
        }
        return null;
    }

    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] scrollBy = new int[2];
        scrollBy[0] = 0; //x axis
        scrollBy[1] = ((VegaLayoutManager) layoutManager).getSnapHeight();
        return scrollBy;
    }
}
