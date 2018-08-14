package com.billy.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.billy.controller.core.ConnectionStatus;
import com.billy.controller.core.IServerMessageProcessor;
import com.billy.controller.core.ServerConnectionService;
import com.billy.controller.core.ServerMessageProcessorManager;
import com.billy.controller.env.EnvSwitchActivity;
import com.billy.controller.info.InfoPanel;
import com.billy.controller.log.collector.LogActivity;

import static com.billy.controller.core.ConnectionStatus.RUNNING;
import static com.billy.controller.core.ConnectionStatus.STOPPED;
import static com.billy.controller.core.ConnectionStatus.WAITING_CLIENT;

/**
 * @author billy.qi
 * @since 17/5/26 12:49
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, IServerMessageProcessor {

    private TextView mBtnOnOff;
    private ConnectionStatus status;
    private InfoPanel infoPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String appName = getIntent().getStringExtra("appName");
        if (!TextUtils.isEmpty(appName)) {
            String title = getTitle().toString();
            setTitle("(" + appName + ")" + title);
        }

        mBtnOnOff = (TextView) findViewById(R.id.btn_connection);
        mBtnOnOff.setOnClickListener(this);
        setOnClickListeners(this, R.id.btn_log, R.id.btn_env, R.id.btn_info);
        status = STOPPED;
        ServerMessageProcessorManager.addProcessor(this);
        processOnOff();//开启连接服务 或 初始化显示当前连接状态
    }

    private void setOnClickListeners(View.OnClickListener listener, @IdRes int... btnIds) {
        if (btnIds != null && btnIds.length > 0) {
            for (int id : btnIds) {
                View view = findViewById(id);
                if (view != null) {
                    view.setOnClickListener(listener);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        Class<? extends Activity> toActivity = null;
        switch (v.getId()) {
            case R.id.btn_log: toActivity = LogActivity.class; break;
            case R.id.btn_env: toActivity = EnvSwitchActivity.class; break;
            case R.id.btn_connection: processOnOff(); return;
            case R.id.btn_info: toggleShowInfo(); return;
        }
        if (toActivity != null) {
            startActivity(new Intent(this, toActivity));
        }
    }

    private void toggleShowInfo() {
        if (infoPanel == null) {
            infoPanel = new InfoPanel(getApplication());
        }
        infoPanel.toggleShow(this);
    }

    private void processOnOff() {
        processOnOff(false);
    }

    private void processOnOff(boolean forceStop) {
        Intent intent = new Intent(this, ServerConnectionService.class);
        if (forceStop || status == RUNNING || status == WAITING_CLIENT) {
            intent.putExtra("stop", true);
        }
        startService(intent);
    }

    Runnable refreshStatusButton = new Runnable() {
        @Override
        public void run() {
            if (status != null) {
                mBtnOnOff.setText(status.resId);
                Drawable image = getResources().getDrawable(status.iconResId);
                Rect bounds = mBtnOnOff.getCompoundDrawables()[3].getBounds();
                image.setBounds(bounds);//非常重要，必须设置，否则图片不会显示
                mBtnOnOff.setCompoundDrawables(null, null, null, image);
            }
        }
    };

    @Override
    public void onStatus(ConnectionStatus st) {
        if (st != null) {
            status = st;
            runOnUiThread(refreshStatusButton);
        }
    }

    @Override
    public void onMessage(String message) {
    }

    @Override
    public String getDebugKey() {
        return ServerMessageProcessorManager.KEY_STATUS;
    }

    @Override
    protected void onDestroy() {
        ServerMessageProcessorManager.removeProcessor(this);
        processOnOff(true);
        if (infoPanel != null) {
            infoPanel.destroy();
        }
        super.onDestroy();
    }
}
