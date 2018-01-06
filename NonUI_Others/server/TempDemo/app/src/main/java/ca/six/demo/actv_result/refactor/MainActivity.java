package ca.six.demo.actv_result.refactor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ca.six.demo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMain = (TextView) findViewById(R.id.tvMain);
        tvMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent(this, ActivityResultDemo.class);
        startActivityForResult(it, 11);

        ActivityResultHelper.startActivityForResult(this, ActivityResultDemo.class,
                new IActivityResultCallback() {
                    @Override
                    public void onSucc(Intent it) {
                        tvMain.setText("Message from page B");
                    }

                    @Override
                    public void onFail(Intent it) {
                        tvMain.setText("Error from page B");
                    }
                });

    }


}
