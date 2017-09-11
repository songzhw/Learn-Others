package ca.six.ui.others.loading.mine1;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

/**
 * Created by songzhw on 2016/4/4.
 */
public class LoadingDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadingView view = new LoadingView(this);
        setContentView(view, new FrameLayout.LayoutParams(400, 400));
    }
}
