package ca.six.ui.others.gooey_menu.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import ca.six.ui.others.R;
import ca.six.ui.others.gooey_menu.GooeyMenu;


public class GooeyMenuDemo extends Activity implements GooeyMenu.GooeyMenuInterface {

    private GooeyMenu mGooeyMenu;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gooey_menu);
        mGooeyMenu = (GooeyMenu) findViewById(R.id.gooey_menu);
        mGooeyMenu.setOnMenuListener(this);
    }

    @Override
    public void menuOpen() {
        showToast("Menu Open");

    }

    @Override
    public void menuClose() {
        showToast( "Menu Close");
    }

    @Override
    public void menuItemClicked(int menuNumber) {
        showToast( "Menu item clicked : " + menuNumber);

    }

   private void showToast(String msg){
        if(mToast!=null){
            mToast.cancel();
        }
       mToast= Toast.makeText(this,msg,Toast.LENGTH_SHORT);
       mToast.setGravity(Gravity.CENTER,0,0);
       mToast.show();
    }
}
