package ca.six.demo.actv_result.refactor;

import android.content.Intent;

/**
 * Created by songzhw on 2018/1/5.
 */

public interface IActivityResultCallback {
    void onSucc(Intent it);
    void onFail(Intent it);
}
