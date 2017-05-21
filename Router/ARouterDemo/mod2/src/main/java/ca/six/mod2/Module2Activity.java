package ca.six.mod2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path="/baba/biz2")
public class Module2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);
        TextView tv = (TextView)findViewById(R.id.tv_simple);
        tv.setText("Module 02");
    }

    public void onClickSimpleButton(View v) {
        ARouter.getInstance().build("/ali/biz1").navigation();
    }

    public void onClickSimpleButton2(View v) {
    }

}