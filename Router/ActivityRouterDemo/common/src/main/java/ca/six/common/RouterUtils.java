package ca.six.common;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.thejoyrun.router.Router;

public class RouterUtils {
    public static void jumpWithLogin(Context ctx, String url){
        if(UserMgr.isLogin()) {
            Router.startActivity(ctx, url);
        } else {
            // e.g. "test://third"  -->  "test://second/third
            int start = url.indexOf("//") + 2;
            StringBuilder sb = new StringBuilder();
            sb.append(url.substring(0, start));
            sb.append("second/");
            sb.append(url.substring(start));
            String newUrl = sb.toString();
            Router.startActivity(ctx, newUrl);
        }
    }

    // 并不是要在这个方法里， 把"test://second/third"替换成"test://questions/third"
    // 看inject()源码就知道， 我替换早了，而且替换”second"的工作， 要给inject来做啊
    public static void buildQuestionUrl(Activity activity) {
        String url = activity.getIntent().getDataString(); // e.g.  "test://second/third"
        // what I need is "test://second/questions/third"
        int start = url.indexOf("second/") + 7;
        String uri = url.substring(0, start) + "questions/"
                + url.substring(start);
        activity.getIntent().setData(Uri.parse(uri));
    }

}