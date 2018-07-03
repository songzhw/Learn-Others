package pl.hypeapp.episodie.ui.features.timecalculator.adapter

import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_time_calculator.view.image_view_item_time_calculator_cover
import kotlinx.android.synthetic.main.item_time_calculator.view.text_view_item_time_calculator_runtime
import kotlinx.android.synthetic.main.item_time_calculator.view.text_view_item_time_calculator_title
import pl.hypeapp.domain.model.tvshow.TvShowModel
import pl.hypeapp.episodie.R
import pl.hypeapp.episodie.extensions.inflate
import pl.hypeapp.episodie.extensions.setRuntime
import pl.hypeapp.episodie.glide.GlideApp
import pl.hypeapp.episodie.ui.base.adapter.ViewType
import pl.hypeapp.episodie.ui.base.adapter.ViewTypeDelegateAdapter
import pl.hypeapp.episodie.ui.viewmodel.TvShowViewModel

class TimeCalculatorDelegateAdapter(val onSelectedListener: OnSelectedListener)
    : ViewTypeDelegateAdapter {

    interface OnSelectedListener {
        fun oItemSelected(basicSearchResultModel: TvShowModel, transitionView: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = TimeCalculatorViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder.setIsRecyclable(false)
        holder as TimeCalculatorViewHolder
        holder.bind(item as TvShowViewModel)
    }

    inner class TimeCalculatorViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(parent.inflate(R.layout.item_time_calculator)) {

        fun bind(item: TvShowViewModel) = with(itemView) {
            text_view_item_time_calculator_title.text = item.tvShow?.name
            text_view_item_time_calculator_runtime.setRuntime(item.tvShow?.fullRuntime)
            GlideApp.with(this).load(item.tvShow?.imageMedium)
                    .placeholder(ColorDrawable(ContextCompat.getColor(itemView.context, R.color.primary_dark)))
                    .thumbnail(0.5f)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(image_view_item_time_calculator_cover)
            super.itemView.setOnClickListener {
                onSelectedListener.oItemSelected(item.tvShow!!, image_view_item_time_calculator_cover)
            }
        }

    }
}
