package ca.six.ui.others.rotate_3d.sample;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import ca.six.ui.others.R;
import ca.six.ui.others.rotate_3d.Rotatable;

/**
 * Created by Yahya Bayramoglu on 04/12/15.
 */
public class Rotatable3dDemo extends Activity {

    private Handler handler;
    private final int ANIM_DURATION = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_demo);

        handler = new Handler();
        runAnimationOn(R.id.view1, Rotatable.ROTATE_Y, 3600, 300);
        runAnimationOn(R.id.view4, Rotatable.ROTATE_Y, 3960, 600);
        runAnimationOn(R.id.view2, Rotatable.ROTATE_BOTH, 1440, 1200);
        runAnimationOn(R.id.view3, Rotatable.ROTATE_X, 2880, 900);

    }

    private void runAnimationOn(final int resId, final int direction, final int degree, int delay) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Rotatable rotatable = new Rotatable.Builder(findViewById(resId))
                        .direction(Rotatable.ROTATE_BOTH)
                        .build();
                rotatable.rotate(direction, degree, ANIM_DURATION);
            }
        }, delay);
    }


}