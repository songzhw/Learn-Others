package ca.six.ui.others.spring.springhead;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.six.ui.others.R;

public class SpringHeaderAdapter extends RecyclerView.Adapter<SpringHeaderAdapter.ItemViewHolder> {

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View item = LayoutInflater.from(context).inflate(R.layout.item, null, false);
        return new ItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.tvItem.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return 100;
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView) itemView.findViewById(R.id.tvItemSpringHeader);
        }
    }
}
