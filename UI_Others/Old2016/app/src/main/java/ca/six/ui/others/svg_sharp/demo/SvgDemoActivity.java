package ca.six.ui.others.svg_sharp.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import ca.six.ui.others.R;
import ca.six.ui.others.svg_sharp.Sharp;

/**
 * Created by songzhw on 2/7/16.
 */
public class SvgDemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg_demo);

        ImageView iv = (ImageView) findViewById(R.id.iv_svg_demo);
        Sharp.loadResource(getResources(), R.raw.darkglasses)
                .into(iv);
    }
}
