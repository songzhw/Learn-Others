package ca.six.ui.others.spring.popmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ca.six.ui.others.R;

public class PopMenu {
    private PopMenu(Builder builder) {
        this.activity = builder.activity;
        this.menuItems.clear();
        this.menuItems.addAll(builder.itemList);

        this.columnCount = builder.columnCount;
        this.duration = builder.duration;
        this.horizontalPadding = builder.horizontalPadding;
        this.verticalPadding = builder.verticalPadding;
        this.popMenuItemListener = builder.popMenuItemListener;

        screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
    }

    public void show() {
        addViews(); // just decorView adding all the sub menu items. (bg is f0ffffff)

        if (flayAnimateLayout.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) flayAnimateLayout.getParent();
            viewGroup.removeView(flayAnimateLayout);
        }

        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup contentView = (ViewGroup) decorView.findViewById(android.R.id.content);
        contentView.addView(flayAnimateLayout);

        //执行显示动画
        showSubMenus(gridLayout);

        isShowing = true;
    }

    public void hide() {
        //先执行消失的动画
        if (isShowing && gridLayout != null) {
            hideSubMenus(gridLayout, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
                    ViewGroup contentView = (ViewGroup) decorView.findViewById(android.R.id.content);
                    contentView.removeView(flayAnimateLayout);
                }
            });
            isShowing = false;
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    private void addViews() {
        flayAnimateLayout = new FrameLayout(activity);

        gridLayout = new GridLayout(activity);
        gridLayout.setColumnCount(columnCount);
        gridLayout.setBackgroundColor(Color.parseColor("#f0ffffff"));

        int hPadding = dp2px(activity, horizontalPadding);
        int vPadding = dp2px(activity, verticalPadding);
        int itemWidth = (screenWidth - (columnCount + 1) * hPadding) / columnCount;

        int rowCount = menuItems.size() % columnCount == 0 ? menuItems.size() / columnCount :
                menuItems.size() / columnCount + 1;

        int topMargin = (screenHeight - (itemWidth + vPadding) * rowCount + vPadding) / 2;

        for (int i = 0; i < menuItems.size(); i++) {
            final int position = i;
            PopSubView subView = new PopSubView(activity);
            PopMenuItem menuItem = menuItems.get(i);
            subView.setPopMenuItem(menuItem);
            subView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (popMenuItemListener != null) {
                        popMenuItemListener.onItemClick(PopMenu.this, position);
                    }
                    hide();
                }
            });

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = itemWidth;
            lp.leftMargin = hPadding;
            if (i / columnCount == 0) {
                lp.topMargin = topMargin;
            } else {
                lp.topMargin = vPadding;
            }
            gridLayout.addView(subView, lp);
        }

        flayAnimateLayout.addView(gridLayout);

        ivClose = new ImageView(activity);
        ivClose.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        ivClose.setImageResource(R.drawable.tabbar_compose_background_icon_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        layoutParams.bottomMargin = dp2px(activity, 25);
        flayAnimateLayout.addView(ivClose, layoutParams);
    }

    private void showSubMenus(ViewGroup viewGroup) {
        if (viewGroup == null) return;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i); //这时的getY(), getTop()全是0
            animateViewDirection(view, screenHeight, 0);
        }
    }

    private void hideSubMenus(ViewGroup viewGroup, final AnimatorListenerAdapter listener) {
        if (viewGroup == null) return;
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.animate().translationY(screenHeight).setDuration(duration).setListener(listener).start();
        }
    }

    private void animateViewDirection(final View v, float from, float to) {
        SpringForce springForce = new SpringForce(0)
                .setStiffness(SpringForce.STIFFNESS_MEDIUM)
                .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        SpringAnimation springAnimation = new SpringAnimation(v, SpringAnimation.TRANSLATION_Y);
        springAnimation.setSpring(springForce);
        springAnimation.setStartValue(from); //finalPostion在SpringForce中设置， startPostion则在SpringAnimation中设置
        springAnimation.start();
    }

    public static class Builder {
        private Activity activity;
        private int columnCount = DEFAULT_COLUMN_COUNT;
        private List<PopMenuItem> itemList = new ArrayList<>();
        private int duration = DEFAULT_DURATION;
        private int horizontalPadding = DEFAULT_HORIZONTAL_PADDING;
        private int verticalPadding = DEFAULT_VERTICAL_PADDING;
        private PopMenuItemListener popMenuItemListener;

        public Builder attachToActivity(Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder addMenuItem(PopMenuItem menuItem) {
            this.itemList.add(menuItem);
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Builder setOnItemClickListener(PopMenuItemListener listener) {
            this.popMenuItemListener = listener;
            return this;
        }

        public PopMenu build() {
            final PopMenu popMenu = new PopMenu(this);
            return popMenu;
        }
    }

    private int dp2px(Context context, int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private static final int DEFAULT_COLUMN_COUNT = 3;// 默认的列数为4个
    private static final int DEFAULT_DURATION = 300; // 动画时间
    private static final int DEFAULT_HORIZONTAL_PADDING = 40; // item水平之间的间距
    private static final int DEFAULT_VERTICAL_PADDING = 15; // item竖直之间的间距

    private Activity activity;
    private int columnCount;
    private List<PopMenuItem> menuItems = new ArrayList<>();
    private FrameLayout flayAnimateLayout;
    private GridLayout gridLayout;
    private ImageView ivClose;
    private int duration;
    private int horizontalPadding, verticalPadding;
    private int screenWidth, screenHeight;
    private boolean isShowing = false;
    private PopMenuItemListener popMenuItemListener;
}

