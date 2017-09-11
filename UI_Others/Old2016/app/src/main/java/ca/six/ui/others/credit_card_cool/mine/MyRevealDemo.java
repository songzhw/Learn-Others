package ca.six.ui.others.credit_card_cool.mine;

import android.animation.Animator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

/**
 * Created by songzhw on 2016/3/1
 */
public class MyRevealDemo extends Activity {

    private TextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new TextView(this);
        view.setTextSize(50);
        view.setTextColor(Color.RED);
        view.setText("Simple Test of Reveal");
        view.setBackgroundColor(Color.DKGRAY);
        setContentView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReveal();
            }
        });
    }

    private void showReveal(){
        int x = view.getLeft();
        int y = view.getTop();
        int radius= Math.max(view.getWidth(), view.getHeight()) * 4;

        // target is APK 21
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Animator anim = ViewAnimationUtils.createCircularReveal(
                    view,
                    x, y,
                    0, radius
            );
            anim.setDuration(3200);
            anim.start();
        }
    }
}
