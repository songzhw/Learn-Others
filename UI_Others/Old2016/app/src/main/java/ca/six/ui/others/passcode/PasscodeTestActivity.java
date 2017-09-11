package ca.six.ui.others.passcode;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ca.six.ui.others.R;


public class PasscodeTestActivity extends AppCompatActivity {
    private PasscodeTestActivity self;
    private PasscodeView passcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        self = this;

        passcodeView = (PasscodeView) findViewById(R.id.passcode_view);
        passcodeView.setPassCodeCount(4);
        passcodeView.setDrawableSize(68);
        passcodeView.setPasscodeItemMargin(28);
        passcodeView.clearAndShow();
        passcodeView.setPasscodeCallback(new PasscodeCallback() {
            @Override
            public void onComplete(CharSequence sequence) {
                if (sequence.equals("4728")) {
                    Toast.makeText(self, "Passcode verified", Toast.LENGTH_SHORT).show();
                    passcodeView.hideSoftKeyboard();
                } else {
                    Toast.makeText(self, "Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
