package ca.six.ui.others.edittext.bank_num;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

public class BankNumActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        EditText et = new BankCardNumEt(this);
//        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
//            | InputType.TYPE_NUMBER_FLAG_SIGNED);

        EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_PHONE);
        setContentView(et);
    }
}
