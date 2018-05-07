package ca.six.ui.others.rv.ad;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ca.six.ui.others.R;
import ca.six.ui.others.rv.ItemView;
import ca.six.ui.others.rv.OneTypesAdapter;
import ca.six.ui.others.rv.RvViewHolder;

public class AdRvDemo extends Activity {

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
        for(int i = 0; i < 30; i++){
            data.add(new ContentItem(i));
        }
        data.add(12, ad);


        RecyclerView rv = findViewById(R.id.rvOne);
        rv.setLayoutManager(new LinearLayoutManager(this));
        OneTypesAdapter adapter = new OneTypesAdapter(data);
        rv.setAdapter(adapter);
    }
}

class ContentItem implements ItemView {

    private final int id;

    public ContentItem(int idx){
        this.id = idx;
    }


    @Override
    public int getViewType() {
        return R.layout.item_content;
    }

    @Override
    public void bind(RvViewHolder holder) {
        holder.setText(R.id.tvItemContent, "item "+id);
    }

}