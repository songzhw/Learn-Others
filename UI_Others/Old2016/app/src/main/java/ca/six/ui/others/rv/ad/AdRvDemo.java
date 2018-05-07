package ca.six.ui.others.rv.ad;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ca.six.ui.others.R;
import ca.six.ui.others.rv.ItemView;
import ca.six.ui.others.rv.OneTypesAdapter;
import ca.six.ui.others.rv.RvViewHolder;

public class AdRvDemo extends Activity {

    private RecyclerView rv;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv);

        ItemView ad = new ItemView() {
            @Override
            public int getViewType() {
                return R.layout.item_ad;
            }

            @Override
            public void bind(RvViewHolder holder) {
                holder.setSrc(R.id.ivItemAd, R.drawable.vertical_wallpaper03);
            }
        };

        List<ItemView> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(new ContentItem(i));
        }
        data.add(12, ad);


        rv = findViewById(R.id.rvOne);
        linearLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(linearLayoutManager);
        OneTypesAdapter adapter = new OneTypesAdapter(data);
        rv.setAdapter(adapter);

        rv.addOnScrollListener(scrollListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rv.removeOnScrollListener(scrollListener);
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int first = linearLayoutManager.findFirstVisibleItemPosition();
            int last = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            int rvHeight = linearLayoutManager.getHeight();
            for (int i = first; i <= last; i++) {
                View childView = linearLayoutManager.findViewByPosition(i);
                View ivWeNeed = childView.findViewById(R.id.ivItemAd);
                if (ivWeNeed != null) {
                    CoordinateImageView iv = (CoordinateImageView) ivWeNeed;
                    iv.setDiff(rvHeight - childView.getTop());
                }

            }
        }
    };
}

class ContentItem implements ItemView {

    private final int id;

    public ContentItem(int idx) {
        this.id = idx;
    }


    @Override
    public int getViewType() {
        return R.layout.item_content;
    }

    @Override
    public void bind(RvViewHolder holder) {
        holder.setText(R.id.tvItemContent, "item " + id);
    }

}