package ca.six.ui.others.rv.ad;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import ca.six.ui.others.R;
import ca.six.ui.others.rv.ItemView;
import ca.six.ui.others.rv.RvViewHolder;

public class AdRvDemo extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);

        ItemView content = new ItemView() {
            @Override
            public int getViewType() {
                return R.layout.item_content;
            }

            @Override
            public void bind(RvViewHolder holder) {
//                holder.setText(R.id.tvItemContent, );
            }
        };

        ItemView ad = new ItemView() {
            @Override
            public int getViewType() {
                return R.layout.item_ad;
            }

            @Override
            public void bind(RvViewHolder holder) {

            }
        };



        RecyclerView rv = findViewById(R.id.rvOne);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
