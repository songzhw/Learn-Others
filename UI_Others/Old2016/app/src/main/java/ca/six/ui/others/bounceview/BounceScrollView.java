package ca.six.ui.others.bounceview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

/**
 * ScrollView反弹效果的实现
 */
public class BounceScrollView extends ScrollView {
	private View innerView;// 孩子View

	private float y;// 点击时y坐标
	// 矩形(这里只是个形式，只是用于判断是否需要动画.)
	private Rect normalRect = new Rect();

	private boolean isCount = false;// 是否开始计算

	public BounceScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/***
	 * 根据 XML 生成视图工作完成.该函数在生成视图的最后调用，在所有子视图添加完之后. 即使子类覆盖了 onFinishInflate
	 * 方法，也应该调用父类的方法，使该方法得以执行.
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getChildCount() > 0) {
			innerView = getChildAt(0);// ScrollView can only have one child
		}
	}

	/***
	 * 监听touch
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (innerView != null) {
			commOnTouchEvent(ev); // no return value. Thus, it will not affect the touch event chain
		}

		return super.onTouchEvent(ev);
	}

	/***
	 * 触摸事件
	 * 
	 * @param ev
	 */
	public void commOnTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			// 手指松开.
			if (isNeedAnimation()) {
				animation();
				isCount = false;
			}
			break;
		/***
		 * 排除出第一次移动计算，因为第一次无法得知y坐标， 在MotionEvent.ACTION_DOWN中获取不到，
		 * 因为此时是MyScrollView的touch事件传递到到了ListView的孩子item上面.所以从第二次计算开始.
		 * 然而我们也要进行初始化，就是第一次移动的时候让滑动距离归0. 之后记录准确了就正常执行.
		 */
		case MotionEvent.ACTION_MOVE:
			final float preY = y;// 按下时的y坐标
			float nowY = ev.getY();// 时时y坐标
			int deltaY = (int) (preY - nowY);// 滑动距离
			if (!isCount) {
				deltaY = 0; // 在这里要归0.
			}

			y = nowY;
			// 当滚动到最上或者最下时就不会再滚动，这时移动布局
			if (isNeedMove()) {
				// 初始化头部矩形
				if (normalRect.isEmpty()) {
					// 保存正常的布局位置
					normalRect.set(innerView.getLeft(), innerView.getTop(),
							innerView.getRight(), innerView.getBottom());
				}
//				Log.e("jj", "矩形：" + innerView.getLeft() + "," + innerView.getTop()
//						+ "," + innerView.getRight() + "," + innerView.getBottom());
				// 移动布局
				innerView.layout(innerView.getLeft(), innerView.getTop() - deltaY / 2,
						innerView.getRight(), innerView.getBottom() - deltaY / 2);
			}
			isCount = true;
			break;

		default:
			break;
		}
	}

	/***
	 * 回缩动画
	 */
	public void animation() {
		// 开启移动动画
		TranslateAnimation ta = new TranslateAnimation(0, 0, innerView.getTop(),
				normalRect.top);
		ta.setDuration(200);
		innerView.startAnimation(ta);
		// 设置回到正常的布局位置
		innerView.layout(normalRect.left, normalRect.top, normalRect.right, normalRect.bottom);

//		Log.e("jj", "回归：" + normalRect.left + "," + normalRect.top + "," + normalRect.right
//				+ "," + normalRect.bottom);

		normalRect.setEmpty();

	}

	// 是否需要开启动画
	public boolean isNeedAnimation() {
		return !normalRect.isEmpty();
	}

	/***
	 * 是否需要移动布局
	 *
	 * innerView.getMeasuredHeight():获取的是控件的总高度
	 * 
	 * getHeight()：获取的是屏幕的高度
	 * 
	 * @return
	 */
	public boolean isNeedMove() {
		int offset = innerView.getMeasuredHeight() - getHeight();
		int scrollY = getScrollY();
//		Log.e("jj", "scrolly=" + scrollY);

		// 0是顶部，后面那个是底部
		if (scrollY == 0 || scrollY == offset) {
			return true;
		}
		return false;
	}

}
