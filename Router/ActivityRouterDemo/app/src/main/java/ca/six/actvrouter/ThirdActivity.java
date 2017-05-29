package ca.six.actvrouter;//package ca.six.others.nui.router.activityrouter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;
import com.thejoyrun.router.RouterField;


@RouterActivity("third")
public class ThirdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);

        Router.inject(this);
        StringBuilder builder = new StringBuilder();
        builder.append("Third Page\n");
        TextView textView = (TextView) findViewById(R.id.tv_simple);
        textView.setText(builder.toString());
    }

    public void onClickSimpleButton(View v){
    }

    public void onClickSimpleButton2(View v){
    }
}


