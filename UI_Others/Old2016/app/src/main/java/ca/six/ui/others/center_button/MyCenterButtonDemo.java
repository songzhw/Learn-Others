package ca.six.ui.others.center_button;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ca.six.ui.others.R;

/**
 * Created by songzhw on 2016/3/5
 *
 * reference : https://github.com/kobakei/CenteredDrawableButton
 */
public class MyCenterButtonDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyCenterButton view = new MyCenterButton(this);
        setContentView(view);
    }


    private class MyCenterButton extends LinearLayout{

        public MyCenterButton(Context context) {
            super(context);

            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.ring);
            iv.setPadding(0, 0, 100, 0);
            addView(iv);

            TextView tv = new TextView(context);
            tv.setText("Center");
            tv.setTextSize(30);
            addView(tv);

            setGravity(Gravity.CENTER);
        }

    }
}
