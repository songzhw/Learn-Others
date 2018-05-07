package ca.six.ui.others.rv;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class OneTypesAdapter extends RecyclerView.Adapter<RvViewHolder> {
    private List<ItemView> data;

    public OneTypesAdapter(List<ItemView> data) {
        this.data = data;
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getViewType();
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RvViewHolder.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
        data.get(position).bind(holder);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }
}

