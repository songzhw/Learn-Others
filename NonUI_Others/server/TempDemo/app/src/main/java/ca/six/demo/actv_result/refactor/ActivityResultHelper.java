package ca.six.demo.actv_result.refactor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by songzhw on 2018/1/5.
 */

public class ActivityResultHelper {
    public void startActivityForResult(Context context, Class<Activity> target) {
        Intent it = new Intent(context, ImplicitResultActivity.class);
        it.putExtra("target_class", target);
        context.startActivity(it);
    }
    // TODO Supporting IntentFlag, IntentParams ...
}
