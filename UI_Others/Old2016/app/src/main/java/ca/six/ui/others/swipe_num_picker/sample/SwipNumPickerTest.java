package ca.six.ui.others.swipe_num_picker.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import ca.six.ui.others.R;
import ca.six.ui.others.swipe_num_picker.OnValueChangeListener;
import ca.six.ui.others.swipe_num_picker.SwipeNumberPicker;

public class SwipNumPickerTest extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe_num_picker);

		final SwipeNumberPicker swipeNumberPicker = (SwipeNumberPicker) findViewById(R.id.snp_implemented);
		final SwipeNumberPicker custom = (SwipeNumberPicker) findViewById(R.id.snp_custom);

		final TextView result1 = (TextView) findViewById(R.id.tv_result_1);
		final TextView result2 = (TextView) findViewById(R.id.tv_result_2);
		result2.setText(String.valueOf(custom.getValue()));

		swipeNumberPicker.setOnValueChangeListener(new OnValueChangeListener() {
			@Override
			public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
				boolean isValueOk = (newValue & 1) == 0;
				if (isValueOk)
					result1.setText(Integer.toString(newValue));

				return isValueOk;
			}
		});
		swipeNumberPicker.setShowNumberPickerDialog(false);
		swipeNumberPicker.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((SwipeNumberPicker) v).setValue(0, true);
			}
		});

		custom.setOnValueChangeListener(new OnValueChangeListener() {
			@Override
			public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
				result2.setText(String.valueOf(newValue));
				return true;
			}
		});


	}
}
