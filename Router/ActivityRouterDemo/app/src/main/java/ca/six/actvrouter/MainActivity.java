package ca.six.actvrouter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thejoyrun.router.Router;

import ca.six.common.RouterUtils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);

        TextView tv = (TextView)findViewById(ca.six.mod1.R.id.tv_simple);
        tv.setText("Main Module");
    }

    public void onClickSimpleButton(View v) {
//        Router.startActivity(this, "test://second/third");
        RouterUtils.jumpWithLogin(this, "test://third");
    }

    public void onClickSimpleButton2(View v) {
        Router.startActivity(this, "test://biz1");
    }
}
