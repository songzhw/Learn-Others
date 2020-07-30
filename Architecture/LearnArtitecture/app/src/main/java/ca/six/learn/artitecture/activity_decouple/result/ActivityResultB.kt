package ca.six.learn.artitecture.activity_decouple.result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ActivityResultB : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.textSize = 40f
        tv.text = "Page 2"
        setContentView(tv)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (MotionEvent.ACTION_UP == event.action) {
            val it = Intent().putExtra("key", "value23")
            setResult(Activity.RESULT_OK, it);
            this.finish()
        }
        return super.onTouchEvent(event)
    }
}