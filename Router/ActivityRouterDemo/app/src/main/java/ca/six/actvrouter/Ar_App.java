package ca.six.actvrouter;

import android.app.Application;

import com.thejoyrun.router.Mod1RouterInitializer;
import com.thejoyrun.router.Mod2RouterInitializer;
import com.thejoyrun.router.Router;


public class Ar_App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Router.init("test");
        Router.register(new Mod1RouterInitializer());
        Router.register(new Mod2RouterInitializer());
        
    }
}
