package com.billy.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.billy.controller.util.PreferenceUtil;

import static com.billy.controller.util.PackageUtil.getAppName;
import static com.billy.controller.util.PackageUtil.getMd5SignByPackageName;
import static com.billy.controller.util.PackageUtil.getPackageNameList;
import static com.billy.controller.util.PreferenceUtil.KEY_PACKAGE_NAME;

/**
 * @author billy.qi
 * @since 17/6/1 14:16
 */
public class WelcomeActivity extends BaseActivity {

    private AutoCompleteTextView editText;
    private String curAppSign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        editText = (AutoCompleteTextView) findViewById(R.id.et_debug_app_package_name);
        String packageName = PreferenceUtil.getString(KEY_PACKAGE_NAME, "");
        editText.setText(packageName);
        curAppSign = getMd5SignByPackageName(getPackageName());

        String [] arr = getPackageNameList();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arr);
        editText.setAdapter(arrayAdapter);
        findViewById(R.id.btn_connection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pkg = editText.getText().toString().trim();
                String appName = getAppName(pkg);
                if (appName == null) {
                    Toast.makeText(WelcomeActivity.this,
                            R.string.connection_error_app_not_exist, Toast.LENGTH_SHORT).show();
                    return;
                }
                String sign = getMd5SignByPackageName(pkg);
                if (TextUtils.equals(curAppSign, sign)) {
                    PreferenceUtil.putString(KEY_PACKAGE_NAME, pkg);
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.putExtra("appName", appName);
                    startActivity(intent);
                } else {
                    Toast.makeText(WelcomeActivity.this,
                            getString(R.string.connection_error_different_sign, appName), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
