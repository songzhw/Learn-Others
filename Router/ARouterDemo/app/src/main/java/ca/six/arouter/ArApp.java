package ca.six.arouter;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by songzhw on 2017-03-20
 */

public class ArApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.openLog();
        ARouter.openDebug();
        ARouter.init(this);
    }
}
