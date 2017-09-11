package ca.six.ui.others.edittext.fade_et;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;

/**
 * Created by songzhw on 2016/3/7
 */
public class FadeEditTextDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FadeEditText view = new FadeEditText(this);
        view.setSingleLine();
        setContentView(view, new FrameLayout.LayoutParams(600, FrameLayout.LayoutParams.WRAP_CONTENT));
    }
}
