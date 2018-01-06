package ca.six.demo.actv_result.refactor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Implicit Activity. You should never use it.
 * The only way to use this Activity is to use it through its static method startActivityForResult().
 */

public class ActivityResultHelper extends Activity {

    private static IActivityResultCallback resultCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // note that, this activity has no content view.

        Intent it = getIntent();
        Class<Activity> target = (Class<Activity>) it.getSerializableExtra("target_class");
        this.startActivityForResult(new Intent(this, target), 11);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 11) {
            return;
        }

        if (resultCode == RESULT_OK) {
            resultCallback.onSucc(data);
        } else {
            resultCallback.onFail(data);
        }

        this.finish();
    }

    @Override
    protected void onDestroy() {
        resultCallback = null; // set the listener to null, to avoid memory leak
        super.onDestroy();
    }

    public static void startActivityForResult(Context context, Class target, IActivityResultCallback callback) {
        Intent it = new Intent(context, ActivityResultHelper.class);
        it.putExtra("target_class", target);
        resultCallback = callback;
        context.startActivity(it);
    }
}
