package ca.six.mod2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chenenyu.router.Router;
import com.chenenyu.router.annotation.Route;

@Route("mod2")
public class Module2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);
        TextView tv = (TextView)findViewById(R.id.tv_simple);
        tv.setText("Module 02");
    }

    public void onClickSimpleButton(View v) {
        Router.build("mod1").go(this);
    }

    public void onClickSimpleButton2(View v) {
    }
    
}