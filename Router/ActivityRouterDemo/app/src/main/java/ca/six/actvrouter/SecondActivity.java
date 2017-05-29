package ca.six.actvrouter;//package ca.six.others.nui.router.activityrouter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thejoyrun.router.Router;
import com.thejoyrun.router.RouterActivity;

import ca.six.common.RouterUtils;
import ca.six.common.UserMgr;


@RouterActivity({"second", "test://www.thejoyrun.com/second"})
public class SecondActivity extends Activity {
//    @RouterField("uid")
//    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);

        StringBuilder builder = new StringBuilder();
        builder.append("Second Page\n");
        TextView textView = (TextView) findViewById(R.id.tv_simple);
        textView.setText(builder.toString());
    }

    public void onClickSimpleButton(View v) {
        if(UserMgr.isNeedSecurityQuestions()){
            RouterUtils.buildQuestionUrl(this);
        }

        Router.inject(this); //不加这句， 就不能多级跳转
        // 原理就是"test://second/third"， 替换成了"url://third"，再用router跳了
    }

    public void onClickSimpleButton2(View v){
    }
}

