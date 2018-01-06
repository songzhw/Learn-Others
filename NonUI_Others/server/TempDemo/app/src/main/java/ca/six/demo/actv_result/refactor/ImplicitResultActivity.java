package ca.six.demo.actv_result.refactor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Implicit Activity. You should never use it.
 * The only way to use this Activity is to use it through {@link ActivityResultHelper}.
 */

public class ImplicitResultActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent it = getIntent();
        Class<Activity> target = (Class<Activity>) it.getSerializableExtra("target_class");
        this.startActivityForResult(it, 11);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 11) {
            return;
        }

        if (resultCode == RESULT_OK) {

        } else {

        }
    }
}
