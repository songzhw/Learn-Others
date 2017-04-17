package ca.six.nonui.action_chain;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ca.six.nonui.R;

import static ca.six.nonui.action_chain.ActionCommand.onBackground;
import static ca.six.nonui.action_chain.ActionCommand.onForeground;

public class ActionChainDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_btn);
    }

    public void onClickSimpleButton(View v){
        foo("23");
    }

    public void onClickSimpleButton2(View v){}

    private void foo(String name){
        onBackground( (x) -> "szw 01 back"+x)
                .then( onBackground((msg) -> "szw 02" + msg))
                .then(onForeground((msg) -> System.out.println("szw 03 "+msg)))
                .exec("[First]");
    }
}
