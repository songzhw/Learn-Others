package ca.six.mod2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;

@RouterActivity("biz2")
public class Module2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);
        TextView tv = (TextView)findViewById(R.id.tv_simple);
        tv.setText("Module 02");
    }

    public void onClickSimpleButton(View v) {
        Router.startActivity(this, "test://biz1");
    }

    public void onClickSimpleButton2(View v) {
//        if(usrMgr.isLogin()){
//            Router.startActivity(this, "test://login/investing");
//        } else {
//            Router.startActivity(this, "test://forth?uid=23&age=16");
//        }
    }
    
}