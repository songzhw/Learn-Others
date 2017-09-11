package ca.six.ui.others.anim.heart_beat;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * simple custom view of a beating heart which is achieved by a scaling animation
 */
public class HeartBeatView extends AppCompatImageView {

    public HeartBeatView(Context context) {
        super(context);
    }

    public HeartBeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Starts the heat beat/pump animation
     */
    public void start() {
        animate().scaleXBy(1).scaleYBy(1).setDuration(1000).setListener(null);
    }


}