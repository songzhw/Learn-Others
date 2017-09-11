package ca.six.ui.others.anim.android6_open_anim.split;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.FrameLayout;

/**
 * Created by songzhw on 2016/3/13.
 */
public class MdProgressDemo extends Activity {

    private MdProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressView = new MdProgressView(this);
        progressView.setBackgroundColor(Color.CYAN);
        setContentView(progressView, new FrameLayout.LayoutParams(300, 300));

        handler.sendEmptyMessageDelayed(11, 2000);

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            System.out.println("szw handler-->");
            progressView.startAnim();
        }
    };
}
