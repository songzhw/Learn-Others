package ca.six.ui.others.spring.springhead;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ca.six.ui.others.R;

public class SpringHeadDemo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo); // 内只有一个rv

        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.ic_launcher);
        iv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 280));

        RecyclerView rv = (RecyclerView) findViewById(R.id.rvSpringHeader);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new WrapperSpringHeaderAdapter(rv, new SpringHeaderAdapter(), iv));
    }
}
