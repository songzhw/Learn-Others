package ca.six.arouter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

@Route(path="/app/bpage")
public class B extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);

        TextView tv = (TextView) findViewById(R.id.tv_simple);
        tv.setText("Main Module (app) : Page B");
    }

    public void onClickSimpleButton(View v) {
        ARouter.getInstance().build("/ali/biz1").navigation();
    }

    public void onClickSimpleButton2(View v) {
        ARouter.getInstance().build("/baba/biz2").navigation();
    }

}
