package ca.six.learn.artitecture.activity_decouple.result

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity

class MyContract : ActivityResultContract<Int, String>() {
    override fun createIntent(context: Context, input: Int): Intent {
        return Intent(context, ActivityResultB::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        val data = intent?.getStringExtra("key")
        return if (resultCode == RESULT_OK && data != null) data else ""
    }
}

class ActivityResultDemo2 : AppCompatActivity() {
    lateinit var myLauncher: ActivityResultLauncher<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.textSize = 30f
        tv.text = "Page 3"
        setContentView(tv)

        myLauncher = registerForActivityResult(MyContract()) { resultString ->
            println("szw result = $resultString") //=> code = RESULT_OK, result = value23
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (MotionEvent.ACTION_UP == event.action) {
            myLauncher.launch(200)
        }
        return super.onTouchEvent(event)
    }
}