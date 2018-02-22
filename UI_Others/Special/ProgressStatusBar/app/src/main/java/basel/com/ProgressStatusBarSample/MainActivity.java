package basel.com.ProgressStatusBarSample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;


import com.basel.ProgressStatusBar.ProgressStatusBar;



public class MainActivity extends AppCompatActivity {

    ProgressStatusBar mProgressStatusBar;
    CheckBox isShowPer;
    int curentProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressStatusBar = new ProgressStatusBar(this);

        isShowPer = findViewById(R.id.isShowPer);

        Button fake = findViewById(R.id.btn_fake);
        fake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressStatusBar.setFakeProgress(3000,isShowPer.isChecked());
            }
        });

        Button handled = findViewById(R.id.handled);
        handled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curentProgress<100){
                    curentProgress = curentProgress+10;
                }else{
                    curentProgress = 0;
                }
                mProgressStatusBar.setProgress(curentProgress+10,isShowPer.isChecked());
            }
        });


        Button wait = findViewById(R.id.btn_wait);
        wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressStatusBar.setWaiting(6000);
            }
        });
        
        Button toast = findViewById(R.id.toast);
        toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressStatusBar.showToast("1 new message",3000);
            }
        });

        mProgressStatusBar.setProgressListener(new ProgressStatusBar.OnProgressListener() {
            public void onStart() {
                //ex: lock the UI or tent it
            }
            public void onUpdate(int progress) {
                //ex: simulate with another progressView
            }
            public void onEnd() {
                //ex: continue the jop
            }
        });



        boolean canDrawOverlay = Settings.canDrawOverlays(this);
        if(!canDrawOverlay){
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 11);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean canDrawOverlay = Settings.canDrawOverlays(this);
        System.out.println("szw can? = "+ canDrawOverlay);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("szw onActvResult() : ok? = "+ (resultCode == RESULT_OK)); // 通没通过, 都不是result_ok
    }


    @Override
    protected void onPause() {
        mProgressStatusBar.remove();
        super.onPause();
    }
}
