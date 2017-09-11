package ca.six.ui.others.pulse;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import ca.six.ui.others.R;

/**
 * Created by songzhw on 2016-05-13.
 */
public class PulseDemo extends Activity implements View.OnClickListener {
    private PulseView view;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        view = new PulseView(this, bitmap);
        view.setOnClickListener(this);
        setContentView(view);
    }

    @Override
    public void onClick(View v) {
        if(count == 0 ) {
            view.startPulse();
        } else {
            view.finishPulse();
        }
        count = (count++) % 2;
    }
}
