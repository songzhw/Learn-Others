package com.billy.controller.env;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.billy.controller.BaseActivity;
import com.billy.controller.R;
import com.billy.controller.core.ConnectionStatus;
import com.billy.controller.core.IServerMessageProcessor;
import com.billy.controller.core.ServerMessageProcessorManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * 环境切换页
 * @author billy.qi
 * @since 17/6/14 17:54
 */
public class EnvSwitchActivity extends BaseActivity implements IServerMessageProcessor {

    private TextView currentEnvTextView;

    private static final HashMap<String, Integer> MAP_ENV_TYPE_NAME = new HashMap<String, Integer>(){ {
        put("sit", R.string.env_type_sit);
        put("pre", R.string.env_type_pre);
        put("prd", R.string.env_type_release);
    } };
    private static final HashMap<String, Integer> MAP_ENV_TYPE_RADIO_ID = new HashMap<String, Integer>(){ {
        put("sit", R.id.env_type_sit);
        put("pre", R.id.env_type_pre);
        put("prd", R.id.env_type_release);
    } };
    private static final SparseArray<String> ENV_TYPE_VALUE_ARR = new SparseArray<String>(){ {
        put(R.id.env_type_sit, "sit");
        put(R.id.env_type_pre, "pre");
        put(R.id.env_type_release, "prd");
    } };

    private static final ArrayList<String> SWITCH_PARAM_LIST = new ArrayList<>(Arrays.asList(
            "bl_network_env_type"
            , "bl_sensors_data_log"
    ));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_env);
        currentEnvTextView = (TextView) findViewById(R.id.env_type_current);
        ServerMessageProcessorManager.addProcessor(this);
        RadioGroup group = (RadioGroup) findViewById(R.id.env_type_group);
        group.setOnCheckedChangeListener(envTypeOnCheckedChangeListener);
        group = (RadioGroup) findViewById(R.id.statistics_log_on_off_group);
        group.setOnCheckedChangeListener(statisticsOnCheckedChangeListener);

        initData();
    }
    RadioGroup.OnCheckedChangeListener statisticsOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            String type = checkedId == R.id.statistics_on ? "1" : "0";
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.putOpt("bl_sensors_data_log", type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String message = "put" + jsonObject.toString();
            ServerMessageProcessorManager.sendMessageToClient(EnvSwitchActivity.this, message);
        }
    };
    RadioGroup.OnCheckedChangeListener envTypeOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            String type = ENV_TYPE_VALUE_ARR.get(checkedId);
            if (TextUtils.isEmpty(type)) {
                Toast.makeText(EnvSwitchActivity.this, R.string.env_invalid_type, Toast.LENGTH_SHORT).show();
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.putOpt("bl_network_env_type", type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String message = "put" + jsonObject.toString();
                ServerMessageProcessorManager.sendMessageToClient(EnvSwitchActivity.this, message);
            }
        }
    };

    private void initData() {
        JSONObject json = new JSONObject();
        for (String type : SWITCH_PARAM_LIST) {
            try{
                json.putOpt(type, "");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        ServerMessageProcessorManager.sendMessageToClient(EnvSwitchActivity.this, "get" + json.toString());
    }

    @Override
    public void onStatus(ConnectionStatus status) {
        if (status == ConnectionStatus.STOPPED) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
    }

    @Override
    public void onMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(message)) {
                    try{
                        JSONObject json = new JSONObject(message);
                        Iterator<String> keys = json.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            String value = json.getString(key);
                            if ("bl_network_env_type".equals(key)) {
                                changeEnvType(value);
                            } else if ("bl_sensors_data_log".equals(key)) {
                                toggleStatistics(value);
                            }
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void toggleStatistics(String value) {
        int resId = "1".equals(value) ? R.id.statistics_on : R.id.statistics_off;
        RadioButton view = (RadioButton) findViewById(resId);
        if (view != null) {
            view.setChecked(true);
        }
    }

    private void changeEnvType(String value) {
        Integer resId = MAP_ENV_TYPE_NAME.get(value);
        if (resId != null) {
            currentEnvTextView.setText(resId);
            Integer id = MAP_ENV_TYPE_RADIO_ID.get(value);
            if (id != null) {
                RadioButton view = (RadioButton) findViewById(id);
                view.setChecked(true);
            }
        } else {
            Toast.makeText(EnvSwitchActivity.this, getString(R.string.env_unkunow_type, value), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public String getDebugKey() {
        return "evn_switch";
    }

    @Override
    protected void onDestroy() {
        ServerMessageProcessorManager.removeProcessor(this);
        super.onDestroy();
    }
}
