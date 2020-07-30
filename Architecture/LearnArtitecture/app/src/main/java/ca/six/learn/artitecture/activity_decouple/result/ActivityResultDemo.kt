package ca.six.learn.artitecture.activity_decouple.result

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity

class ActivityResultDemo : AppCompatActivity() {
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.textSize = 30f
        tv.text = "Page 1"
        setContentView(tv)

        // 自动生成requestCode, 所以不用我们操心了. (若有多种requestCode, 那就使用多个ActivityResultLauncher实例.
        // 算是彻底解决了if-else串这种不符合OC原则的做法了.
        resultLauncher = registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
                // ActivityResult类只有两个成员: int mResultCode, Intent mData
                val data = result.data
                println("szw code = ${ActivityResult.resultCodeToString(result.resultCode)}")
                println("szw result = ${data?.extras?.get("key")}") //=> code = RESULT_OK, result = value23
            }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (MotionEvent.ACTION_UP == event.action) {
            val it = Intent(this, ActivityResultB::class.java)
            resultLauncher.launch(it)
        }
        return super.onTouchEvent(event)
    }
}


