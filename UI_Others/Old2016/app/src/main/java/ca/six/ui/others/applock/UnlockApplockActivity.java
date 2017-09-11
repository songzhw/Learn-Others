package ca.six.ui.others.applock;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import ca.six.ui.others.utils.UiUtil;


/**
 * Created by songzhw on 2016/1/12
 */
public class UnlockApplockActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PINInputView view = new PINInputView(this);
        PINInputController ctrl = new PINInputController(view, null)
                .setInputNumbersCount(4)
                .setPasswordCharactersEnabled(false);
        setContentView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UiUtil.dp2px(this, 50)));
    }

}
