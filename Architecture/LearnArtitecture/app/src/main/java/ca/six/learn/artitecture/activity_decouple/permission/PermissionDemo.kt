package ca.six.learn.artitecture.activity_decouple.permission

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity


// 备注: 请求权限 是ReqeustPermssion()用于请求一个权限
// 而ReqeustMultiplePermssions()是请求多个权限. 而callback中就不再是一个boolean值了, 而是一个object, 可以一一判断看哪个权限有哪个权限被拒
class PermissionDemo : AppCompatActivity() {
    lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        tv.textSize = 30f
        tv.text = "Permission 01"
        setContentView(tv)


        permissionLauncher = registerForActivityResult(RequestPermission()) {isGranted ->
            //若已授权的情况下还launch(同样权限), 也会直接进入这里(只是isGranted = true)
            println("permission $isGranted")
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (MotionEvent.ACTION_UP == event.action) {
            permissionLauncher.launch(WRITE_EXTERNAL_STORAGE)
        }
        return super.onTouchEvent(event)
    }
}


