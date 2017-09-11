package ca.six.ui.others.loading.spinkit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import ca.six.ui.others.R;
import ca.six.ui.others.loading.spinkit.sprites.FoldingCube;

/**
 * Created by songzhw on 4/13/16.
 */
public class SpinkitDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_spinkit);

        SpinKitView spinKitView = (SpinKitView) findViewById(R.id.skv_loading);
        Sprite drawable = new FoldingCube();
        spinKitView.setIndeterminateDrawable(drawable);
    }



    public void clickLoadingSpinkit(View v){

    }
}
