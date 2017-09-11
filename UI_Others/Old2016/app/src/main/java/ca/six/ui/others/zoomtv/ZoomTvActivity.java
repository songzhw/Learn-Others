package ca.six.ui.others.zoomtv;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;

public class ZoomTvActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ZoomTextView tv = new ZoomTextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setText("Hello, Zoom Tv");
        setContentView(tv);
    }
}
