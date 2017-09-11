package ca.six.ui.others.anim.android6_open_anim.letter;

import android.graphics.Canvas;

/**
 * Created by yanxing on 16/2/18.
 */
public abstract class Letter {

    protected int curX;
    protected int curY;
    protected int duration = 2000;

    public Letter(int x, int y) {
        curX = x;
        curY = y;
    }

    public void startAnim() {

    }

    public void drawSelf(Canvas canvas) {

    }
}
