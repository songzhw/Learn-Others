package ca.six.learn.artitecture.lifecycle

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import ca.six.learn.artitecture.R


class KeyboardDemo : AppCompatActivity(R.layout.activity_keyboard) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                println("szw state change $event")
            }
        })

        val rootView = findViewById<View>(Window.ID_ANDROID_CONTENT)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            // 每次软软键盘的弹出/消失, 都会触发onGlobalLayout()这个方法
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            println("szw onGlobalLayout(${imm.isActive}) : ${rootView.isKeyboardOpen()}")
        }
    }
}

fun View.isKeyboardOpen(): Boolean {
    val rect = Rect()
    getWindowVisibleDisplayFrame(rect)
    // 以1080 * 2160的手机来说, 正常情况下, rect就是(0, 66 -- 1080, 2028)
    // 要是键盘弹出来了, 那rect就是 (0, 66 -- 1080, 1343)
    // 备注: 2160 - 2028 = 132, 正好是48dp * 2.75(density). 所以navigation bar是48dp
    println("szw        rect = $rect")
    println("szw        density = " + Resources.getSystem().displayMetrics.density)
    println("szw        height = ${rootView.height}")
    return true
}
/*
  1. 可能的例外情况1: gesture navigation  -- 其实不影响我们, 因为我们是判断差大于100dp, 才算出来. 所以不影响正常时
  仍以1080 * 2160的手机来说,
  正常情况下, rect就是(0, 66 -- 1080, 2116)      : 这时2160 - 2116 = 44, 只有16dp
  有键盘弹出时, rect是(0, 66 -- 1080, 1343)
    (好吧, 这时即使是gesture navigation, 软键盘为了有一个dismiss的图标(向下箭头), 导致nav bar看起来仍成了48dp

  例外情况2: 分屏
  1). 当我们应用在下屏时, 正常时是(0, 1061 -- 1080, 2028), 弹出键盘时是(0, 376 - 1080, 1343)
        (上屏的应用会变得很小, 不再是占一半高度了)
  2). 当我们应用在上屏时, 键盘弹出只遮住下屏的. 所以我们的应用的onGlobalLayout根本都没有调用到.

  所以整个依赖onGlobalLayout来判断键盘弹出来了的机制是失败的, 因为它不能覆盖到分屏的情况
  (除非你在manifest中给<application>或<activity>添加android:resizeableActivity=false, 强制不允许分屏)
 */