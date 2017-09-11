package ca.six.ui.others.spring.springhead;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class WrapperSpringHeaderAdapter extends RecyclerView.Adapter {
    private static final int FIRST_ITEM = 0;
    private static final int VIEW_TYPE_SPRING_HEADER = 100;
    private static final float SCROLL_RATIO = 0.5f;
    private static final int DURATION = 200;
    private static final float MAX_SCALE_VALUE = 1.5f;

    private float lastY = 0;  // 做为滑动多少的依据
    private State state = State.NORMAL;
    private Rect normalRect; // 存储正常时, spingHeaderView的rect信息的. 用于action_up时, 回复到原位来.

    private RecyclerView recyclerView;
    private RecyclerView.Adapter realAdapter;
    private View springHeaderView;

    public WrapperSpringHeaderAdapter(RecyclerView recyclerView, RecyclerView.Adapter adapter, View springView) {
        normalRect = new Rect();
        this.recyclerView = recyclerView;
        realAdapter = adapter;
        springHeaderView = springView;
        initTouchListener();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == FIRST_ITEM) {
            return VIEW_TYPE_SPRING_HEADER;
        }
        return realAdapter.getItemViewType(position); //因为realAdapter没有设置,所以这里默认为0
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SPRING_HEADER) {
            return new SpringHeaderHolder(springHeaderView);
        }
        return realAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SPRING_HEADER) {
            return;
        }
        realAdapter.onBindViewHolder(holder, position - 1);
    }

    @Override
    public int getItemCount() {
        return realAdapter.getItemCount() + 1;
    }


    private void initTouchListener() {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        onUp();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        return onMove(event);
                }
                return false;
            }
        });
    }

    // return : Did the WrapperAdapter consume the the touch event?
    // 1. 若firstCompletelyVisibleItemPosition不是0, 那不管, 即return false
    // 2. 1成立. 但滑动是往上滑动, 没反应, return false
    // 3. 1成立, 滑动是往下, 就开始spring scale的动画. 做完动画后, return true
    // 4. 再滑动再滑动, 超过了maxValue, 再滑也没反应了. 即不做动画, 直接return true
    private boolean onMove(MotionEvent event) {
        if (state != State.DRAGGING) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == FIRST_ITEM) {
                lastY = event.getY();
            } else {
                return false; // 第一项不完整展现时,不可以出现spring header相关的动画. 一切都按原来的rv的惯例走.
            }
        }

        // 当第一项完整可见时, 开始滚动
        // 滑动距离只是滚动距离的50%. 注意上滑动还是下滑动
        int distance = (int) ((event.getY() - lastY) * SCROLL_RATIO); //scroll_ratio是0.5f
        if (distance <= 0) { //只有distance大于0，才认为在拖动
            return false;
        }


        // 开始滚动了
        state = State.DRAGGING;
        if (normalRect.isEmpty()) {
            normalRect.set(springHeaderView.getLeft(), springHeaderView.getTop(), springHeaderView.getRight(), springHeaderView.getBottom());
        }

        //计算缩放
        float scaleX = (distance + normalRect.width()) / normalRect.width();
        float scaleY = (distance + normalRect.height()) / normalRect.height();
        if (scaleY >= MAX_SCALE_VALUE) { // MAX_SCALE_VALUE : 1.5f
            return true; // 滑动到一定边界,再滑,我们的spring header也不会变大了. 这样更真实自然.
        } // 也不会变大, 就是通过这个return true来做的. 我们这已经吞了ev, 所以rv再滑动也没有反应了


        // 增加header高度
        ViewGroup.LayoutParams lp = springHeaderView.getLayoutParams();
        lp.height = normalRect.height() + distance;
        springHeaderView.setLayoutParams(lp);
        springHeaderView.setScaleX(scaleX); // 设置header缩放
        springHeaderView.setScaleY(scaleY);
        return true;
    }

    private void onUp() {
        if (state != State.DRAGGING) {
            return;
        }

        final ViewGroup.LayoutParams lp = springHeaderView.getLayoutParams();
        int height = springHeaderView.getLayoutParams().height;// 图片当前高度
        int newHeight = normalRect.height();// 图片原高度

        // 设置动画
        ValueAnimator animHeight = ObjectAnimator.ofInt(height, newHeight).setDuration(DURATION);
        animHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lp.height = (int) animation.getAnimatedValue();
                springHeaderView.setLayoutParams(lp);
            }
        });
        animHeight.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //清空矩阵以及状态
                normalRect.setEmpty();
                state = State.NORMAL;
                lastY = 0;
            }
        });

        ValueAnimator animX = ObjectAnimator.ofFloat(springHeaderView, View.SCALE_X, springHeaderView.getScaleX(), 1.0f).setDuration(DURATION);
        ValueAnimator animY = ObjectAnimator.ofFloat(springHeaderView, View.SCALE_Y, springHeaderView.getScaleY(), 1.0f).setDuration(DURATION);

        animX.start();
        animY.start();
        animHeight.start();
    }


    private static class SpringHeaderHolder extends RecyclerView.ViewHolder {
        SpringHeaderHolder(View itemView) {
            super(itemView);
        }
    }

    private enum State {
        DRAGGING,
        NORMAL
    }
}
