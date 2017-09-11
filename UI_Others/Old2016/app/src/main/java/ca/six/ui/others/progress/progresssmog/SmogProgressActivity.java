package ca.six.ui.others.progress.progresssmog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;

import ca.six.ui.others.R;

/**
 * Created by songzhw on 2015/12/23
 */
public class SmogProgressActivity extends Activity {
    private CircleLoadingView pb;
    private int percentage = 0;
    private HandlerThread thread;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smog_progress);

        pb = (CircleLoadingView)findViewById(R.id.pb_smog);
        thread = new HandlerThread("progress");
        thread.start(); // without this line, a "Looper.mQueue is null" Exception will be raised

        handler = new Handler(thread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                percentage++;
                if(percentage <= 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pb.setPercent(percentage);
                        }
                    });
                    SystemClock.sleep(20);
                    handler.sendEmptyMessage(11);
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.quit();
        handler.removeMessages(11);
    }

    // <Button> android:onClick="startAnim"
    public void startAnim(View v ){
        percentage = 0;
        handler.sendEmptyMessage(11);
    }

}
