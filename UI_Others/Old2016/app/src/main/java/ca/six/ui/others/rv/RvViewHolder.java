package ca.six.ui.others.rv;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by songzhw on 2016-08-12
 */
public class RvViewHolder extends RecyclerView.ViewHolder {
    private SparseArrayCompat<View> views;
    private View convertView;

    private RvViewHolder(View itemView) {
        super(itemView);
        this.convertView = itemView;
        views = new SparseArrayCompat<View>();
    }

    public static RvViewHolder createViewHolder(View itemView) {
        RvViewHolder holder = new RvViewHolder(itemView);
        return holder;
    }

    public static RvViewHolder createViewHolder(ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        RvViewHolder holder = new RvViewHolder(itemView);
        return holder;
    }

    public <T extends View> T getView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = convertView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    // ============================================
    public void setText(int id, String str) {
        TextView tv = getView(id);
        tv.setText(str);
    }

    public void setSrc(int id, int drawId) {
        ImageView iv = getView(id);
        iv.setImageResource(drawId);
    }

    public void setBackground(int id, int bgResId) {
        View view = getView(id);
        view.setBackgroundResource(bgResId);
    }

    public void setClickListener(int id, View.OnClickListener listener) {
        View view = getView(id);
        view.setOnClickListener(listener);
    }

    public void setVisibility(int id, int visibility){
        View view = getView(id);
        view.setVisibility(visibility);
    }


}
