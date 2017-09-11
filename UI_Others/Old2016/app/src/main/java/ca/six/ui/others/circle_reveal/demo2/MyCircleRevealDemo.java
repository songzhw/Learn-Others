package ca.six.ui.others.circle_reveal.demo2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.six.ui.others.R;
import ca.six.ui.others.circle_reveal.animation.SupportAnimator;
import ca.six.ui.others.circle_reveal.animation.ViewAnimationUtils;

/**
 * Created by songzhw on 3/19/16.
 */
public class MyCircleRevealDemo extends AppCompatActivity {
    private View frontView, backView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle_reveal);

        final View registeView = findViewById(R.id.view_register);
        final View loginView = findViewById(R.id.view_login);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_mycr);

        frontView = registeView;
        backView = loginView;
        final int rawRadius = 1200;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // view, centerX, centerY, startRadius, endRadius)
                System.out.println("szw rawRadisu = "+rawRadius);
                SupportAnimator anim = ViewAnimationUtils.createCircularReveal(
                        backView, rawRadius, 0,
                        0, 2 * rawRadius
                );
                anim.setDuration(700);
                anim.addListener(new SupportAnimator.SimpleAnimatorListener() {
                    @Override
                    public void onAnimationStart() {
                        backView.setVisibility(View.VISIBLE);
                        frontView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd() {
                        View temp = frontView;
                        frontView = backView;
                        backView = temp;
                    }
                });
                anim.start();
            }
        });
    }
}
