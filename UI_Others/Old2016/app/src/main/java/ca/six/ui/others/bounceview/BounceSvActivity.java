package ca.six.ui.others.bounceview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import ca.six.ui.others.R;

public class BounceSvActivity extends Activity {
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_bounce_sv);
    }  

  
} 