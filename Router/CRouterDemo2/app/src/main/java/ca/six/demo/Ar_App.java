package ca.six.demo;

import android.app.Application;

import com.chenenyu.router.Router;


public class Ar_App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Router.openLog();
        Router.initialize(this);
    }
}
