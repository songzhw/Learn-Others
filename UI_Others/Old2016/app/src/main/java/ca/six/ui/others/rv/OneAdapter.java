package ca.six.ui.others.rv;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class OneAdapter<T> extends RecyclerView.Adapter<RvViewHolder> {
    public int layoutResId;
    public List<T> data;

    public OneAdapter(int layoutResId) {
        this.layoutResId = layoutResId;
        data = new ArrayList<>();
    }

    public OneAdapter(int layoutResId, List<T> data) {
        this.layoutResId = layoutResId;
        this.data = data;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RvViewHolder vh = RvViewHolder.createViewHolder(parent, layoutResId);
        return vh;
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        if (data != null && data.size() > position) {
            apply(holder, data.get(position), position);
        }
    }


    protected abstract void apply(RvViewHolder vh, T t, int position);

}
