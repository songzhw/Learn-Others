package ca.six.ui.others.expandableview.demo2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ca.six.ui.others.R;
import ca.six.ui.others.expandableview.ExpandableView;
import ca.six.ui.others.expandableview.demo.ExpandedChildView;


public class ExpandableViewDemo2 extends Activity {
    private SimpleExpandableView parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_view_two);

        parentView = (SimpleExpandableView) findViewById(R.id.expandView);
        parentView.fillData(R.drawable.ic_launcher, getString(R.string.android_names), true);

        String[] androidVersionNameList = getResources().getStringArray(R.array.android_version_names);
        for(String str : androidVersionNameList){
            ExpandedChildView itemView = new ExpandedChildView(this);
            itemView.setText(str, true);
            parentView.addContentView(itemView);
        }

    }
}