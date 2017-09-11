package ca.six.ui.others.progress.progress_dilating.demo;

import android.app.Activity;
import android.os.Bundle;

import ca.six.ui.others.R;
import ca.six.ui.others.progress.progress_dilating.DilatingDotsProgressBar;


public class DialaitingDotProgressTest extends Activity {
    private DilatingDotsProgressBar mDilatingDotsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_dilating_dots);

        mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress_dilating_dots);
        mDilatingDotsProgressBar.setDotRadius(50);
        mDilatingDotsProgressBar.setDotSpacing(40);
        mDilatingDotsProgressBar.setNumberOfDots(6);
        mDilatingDotsProgressBar.show(500);

    }

}
