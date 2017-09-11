package ca.six.ui.others.loading.spinkit;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import ca.six.ui.others.loading.spinkit.sprites.FoldingCube;

public class SpinKitView extends ProgressBar {

    private int mColor;
    private Sprite mSprite;

    public SpinKitView(Context context) {
        this(context, null);
    }

    public SpinKitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mColor = Color.BLUE;
        setIndeterminateDrawable(new FoldingCube());
        setIndeterminate(true);
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        super.setIndeterminateDrawable(d);
        if(d instanceof Sprite) {
            setIndeterminateDrawable((Sprite) d);
        }
    }

    public void setIndeterminateDrawable(Sprite d) {
        super.setIndeterminateDrawable(d);
        mSprite = d;
        mSprite.setColor(mColor);
    }

    @Override
    public Sprite getIndeterminateDrawable() {
        return mSprite;
    }

    @Override
    public void setIndeterminateDrawableTiled(Drawable d) {
        super.setIndeterminateDrawableTiled(d);
    }
}