package ca.six.ui.others.edittext.creditcardet;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import ca.six.ui.others.R;

public class CreditCardEtActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_et);

        final CcnEditText et = (CcnEditText) findViewById(R.id.etCcnEditText);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("szw : "+et.validate());
            }
        });
    }
}
