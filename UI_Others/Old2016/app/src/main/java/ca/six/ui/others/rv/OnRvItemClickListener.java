package ca.six.ui.others.rv;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by songzhw on 2016-07-13
 * <p>
 * NOTE: If you have a head wrapper, or a footer wrapper, or a load more wrapper,
 * please note that the position may different than usual!
 */
public abstract class OnRvItemClickListener implements RecyclerView.OnItemTouchListener {
    private GestureDetectorCompat gestureDetector;
    private RecyclerView rv;

    public OnRvItemClickListener(RecyclerView rv) {
        this.rv = rv;
        gestureDetector = new GestureDetectorCompat(rv.getContext(), new RvGestureListener());
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public void onLongClick(RecyclerView.ViewHolder vh) {
    }

    // ========================= abstract methods =================================
    public abstract void onItemClick(RecyclerView.ViewHolder vh);

    private class RvGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            // vibrator.vibrate(70); // Remember to add the Vibrator permission.

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = rv.getChildViewHolder(child);
                onLongClick(vh);
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null) {
                RecyclerView.ViewHolder vh = rv.getChildViewHolder(child);
                onItemClick(vh);
            }
            return true;
        }
    }

}
