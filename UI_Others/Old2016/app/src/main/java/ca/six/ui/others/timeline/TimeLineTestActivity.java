package ca.six.ui.others.timeline;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ca.six.ui.others.R;

/**
 * Created by songzhw on 2015/12/25
 */
public class TimeLineTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout llay = new LinearLayout(this);
        llay.setOrientation(LinearLayout.VERTICAL);

        View v1 = this.getLayoutInflater().inflate(R.layout.item_timeline, null);
        TimelineView t1 = (TimelineView) v1.findViewById(R.id.time_marker);
        t1.initLine(LineType.BEGIN);
        llay.addView(v1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 250));

        View v2 = this.getLayoutInflater().inflate(R.layout.item_timeline, null);
        llay.addView(v2, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 250));

        View v3 = this.getLayoutInflater().inflate(R.layout.item_timeline, null);
        llay.addView(v3, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 250));

        View v4 = this.getLayoutInflater().inflate(R.layout.item_timeline, null);
        TimelineView t4 = (TimelineView) v4.findViewById(R.id.time_marker);
        t4.initLine(LineType.END);
        llay.addView(v4, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 250));

        setContentView(llay);
    }

}
