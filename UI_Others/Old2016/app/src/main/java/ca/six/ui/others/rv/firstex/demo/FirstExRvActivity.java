package ca.six.ui.others.rv.firstex.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ca.six.ui.others.R;
import ca.six.ui.others.rv.firstex.FeatureLinearLayoutManager;
import ca.six.ui.others.rv.firstex.FirstExRecyclerView;


public class FirstExRvActivity extends AppCompatActivity {

    List<String> dummyData = new ArrayList<>();
    FirstExRecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_ex_rv);

        createDummyDataList();
        rv = (FirstExRecyclerView) findViewById(R.id.featured_recycler_view);
        FeatureLinearLayoutManager layoutManager = new FeatureLinearLayoutManager(this);
        rv.setLayoutManager(layoutManager);
        CustomRecyclerViewAdapter adapter = new CustomRecyclerViewAdapter(this, dummyData);
        rv.setAdapter(adapter);
    }

    private void createDummyDataList() {
        for (int i = 1; i <= 20; i++) {
            dummyData.add("Item " + i);
        }
    }
}
