package ca.six.ui.others.anim.android6_open_anim.demo;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ca.six.ui.others.R;
import ca.six.ui.others.anim.android6_open_anim.CoolAnimView;

public class And6OpenAnimDemo extends AppCompatActivity {

    private Button mBtnDlgShow;
    private CoolAnimView mCoolAnimView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_and_open_anim);

        mBtnDlgShow = (Button) findViewById(R.id.btn_dialog);
        mCoolAnimView = (CoolAnimView) findViewById(R.id.cool_view);
        mCoolAnimView.setOnCoolAnimViewListener(new CoolAnimView.OnCoolAnimViewListener() {
            @Override
            public void onAnimEnd() {
                Toast.makeText(And6OpenAnimDemo.this, "end", Toast.LENGTH_SHORT).show();
            }
        });
        // 1s后结束动画
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 调用此方法,使动画进入结束阶段
                mCoolAnimView.stopAnim();
            }
        }).start();

        RelativeLayout layout = new RelativeLayout(And6OpenAnimDemo.this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(new CoolAnimView(And6OpenAnimDemo.this), params);
        final AlertDialog dialog = new AlertDialog.Builder(And6OpenAnimDemo.this,R.style.Translucent_NoTitle)
                .setView(layout)
                .create();

        mBtnDlgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }
}
