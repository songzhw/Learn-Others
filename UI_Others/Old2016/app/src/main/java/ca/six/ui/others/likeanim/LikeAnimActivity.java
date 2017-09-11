package ca.six.ui.others.likeanim;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

import ca.six.ui.others.utils.UiUtil;

/**
 * Created by songzhw on 2015/12/28
 */
public class LikeAnimActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LikeButtonView view = new LikeButtonView(this);
        int size = UiUtil.dp2px(this, 200);
        setContentView(view, new FrameLayout.LayoutParams(size, size));

    }

}
