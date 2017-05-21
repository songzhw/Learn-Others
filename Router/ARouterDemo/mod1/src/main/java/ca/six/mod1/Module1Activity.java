package ca.six.mod1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path="/ali/biz1")
public class Module1Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);
        TextView tv = (TextView)findViewById(R.id.tv_simple);
        tv.setText("Module 01");
    }

    public void onClickSimpleButton(View v) {
        ARouter.getInstance().build("/ali/biz1").navigation();
    }

    public void onClickSimpleButton2(View v) {
        ARouter.getInstance().build("/baba/biz2").navigation();
    }

}