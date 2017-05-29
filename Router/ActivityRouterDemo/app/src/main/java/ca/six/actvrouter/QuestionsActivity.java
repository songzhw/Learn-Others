package ca.six.actvrouter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;

@RouterActivity("questions")
public class QuestionsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);

        TextView tv = (TextView) findViewById(ca.six.mod1.R.id.tv_simple);
        tv.setText("What is your name?");
    }

    public void onClickSimpleButton(View v) {
        Toast.makeText(this, "The Answer is  Right. ogLinSuccess", Toast.LENGTH_SHORT).show();
        Router.inject(this);
    }

    public void onClickSimpleButton2(View v) {

    }

}