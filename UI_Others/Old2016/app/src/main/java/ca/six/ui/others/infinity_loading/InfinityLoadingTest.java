package ca.six.ui.others.infinity_loading;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import ca.six.ui.others.R;


public class InfinityLoadingTest extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private InfinityLoading infinityLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infinity_loading);

        infinityLoading = (InfinityLoading) findViewById(R.id.inifinityLoading);
        CheckBox checboxReverse = (CheckBox) findViewById(R.id.checkboxReverse);
        CheckBox checboxDrawBack = (CheckBox) findViewById(R.id.checkboxDrawBack);

        checboxReverse.setOnCheckedChangeListener(this);
        checboxDrawBack.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        switch (id) {
            case R.id.checkboxReverse:
                infinityLoading.setReverse(isChecked);
                break;
            case R.id.checkboxDrawBack:
                infinityLoading.setDrawBack(isChecked);
                break;
        }
    }
}
