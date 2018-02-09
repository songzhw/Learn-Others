package com.jyuesong.dragview2fill.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by jiang on 16/8/29.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {
        onBindView(holder, holder.getLayoutPosition());
    }

    public abstract void onBindView(BaseViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        return getLayoutID(position);
    }

    public abstract int getLayoutID(int position);

}