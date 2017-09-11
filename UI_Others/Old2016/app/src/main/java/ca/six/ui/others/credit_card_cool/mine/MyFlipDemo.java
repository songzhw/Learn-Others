package ca.six.ui.others.credit_card_cool.mine;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import ca.six.ui.others.R;
import ca.six.ui.others.credit_card_cool.core.FlipAnimator;

/**
 * Created by songzhw on 2016/3/1
 */
public class MyFlipDemo extends Activity {

    private boolean isLeft2Right;
    private View layoutContainer, frontView, backView, layoutContentContainer, frontContent, backContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_creditcard);

        layoutContainer = findViewById(R.id.card_outline_container);
        frontView = findViewById(R.id.front_card_outline);
        backView = findViewById(R.id.back_card_outline);

        layoutContentContainer = findViewById(R.id.card_container);
        frontContent = findViewById(R.id.front_card_container);
        backContent = findViewById(R.id.back_card_container);


        this.isLeft2Right = true;
        layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flip(isLeft2Right);
                isLeft2Right = !isLeft2Right;
            }
        });

    }

    public void flip(boolean isLeftToRight){
        int duration = 600;

        FlipAnimator flipAll = new FlipAnimator(frontView, backView, frontView.getWidth()/2, backView.getHeight()/2);
        flipAll.setDuration(duration);
        flipAll.setTranslateDirection(FlipAnimator.DIRECTION_Z);
        flipAll.setRotationDirection(FlipAnimator.DIRECTION_Y);
        if(!isLeftToRight){
            flipAll.reverse();
        }
        layoutContainer.startAnimation(flipAll);

        FlipAnimator flipContent = new FlipAnimator(frontContent, backContent, frontContent.getWidth()/2, backContent.getHeight()/2);
        flipContent.setDuration(duration);
        flipContent.setTranslateDirection(FlipAnimator.DIRECTION_Z);
        flipContent.setRotationDirection(FlipAnimator.DIRECTION_Y);
        if(!isLeftToRight){
            flipContent.reverse();
        }
        layoutContentContainer.startAnimation(flipContent);

    }
}
