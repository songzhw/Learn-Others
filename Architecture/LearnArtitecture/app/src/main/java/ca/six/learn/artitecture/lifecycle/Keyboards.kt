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
    println("szw density = " + Resources.getSystem().displayMetrics.density)
    println("szw height = ${rootView.height}")
    return true
}