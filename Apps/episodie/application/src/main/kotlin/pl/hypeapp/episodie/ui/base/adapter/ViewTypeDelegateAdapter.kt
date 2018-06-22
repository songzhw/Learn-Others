package pl.hypeapp.episodie.ui.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import pl.hypeapp.domain.model.tvshow.TvShowModel

interface ViewTypeDelegateAdapter {

    interface OnViewSelectedListener {
        fun onItemSelected(item: TvShowModel?, vararg views: View)
    }

    interface OnItemChangedListener {
        fun onItemChanged(viewHolder: RecyclerView.ViewHolder, adapterPosition: Int)
    }

    interface OnRetryListener {
        fun onRetry()
    }

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)

}
