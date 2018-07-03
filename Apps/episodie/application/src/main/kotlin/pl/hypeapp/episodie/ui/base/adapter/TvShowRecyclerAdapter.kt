package pl.hypeapp.episodie.ui.base.adapter

import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import pl.hypeapp.episodie.ui.viewmodel.TvShowViewModel

class TvShowRecyclerAdapter(val totalItemCount: Int,
                            viewItemDelegateAdapter: ViewTypeDelegateAdapter,
                            onRetryListener: ViewTypeDelegateAdapter.OnRetryListener)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType>

    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    private val loadingItem = object : ViewType {
        override fun getViewType(): Int = ViewType.LOADING
    }

    private val errorItem = object : ViewType {
        override fun getViewType(): Int = ViewType.ERROR
    }

    init {
        delegateAdapters.put(ViewType.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ViewType.ITEM, viewItemDelegateAdapter)
        delegateAdapters.put(ViewType.ERROR, ErrorDelegateAdapter(onRetryListener))
        items = ArrayList()
        addLoadingView()
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    fun addItems(items: List<TvShowViewModel>) {
        // Remove loading  or error item view
        val initPosition = this.items.size - 1
        this.items.removeAt(initPosition)
        notifyItemRemoved(initPosition)
        // Needs to clear list otherwise elements are repeating (look at MosPopularViewModel)
        this.items.clear()
        items.forEach({
            if (this.items.size < totalItemCount) this.items.add(it)
        })
        if (isThereItemsToLoad()) {
            addLoadingView()
        }
        notifyItemRangeChanged(initPosition, this.items.size)
    }

    fun updateItems(items: List<TvShowViewModel>) {
        this.items.clear()
        items.forEach({
            this.items.add(it)
        })
        notifyDataSetChanged()
    }

    fun addErrorItem() {
        val initPosition = this.items.size - 1
        this.items.removeAt(initPosition)
        notifyItemRemoved(initPosition)
        addErrorView()
        notifyItemInserted(initPosition)
    }

    private fun addErrorView() {
        items.add(errorItem)
    }

    private fun addLoadingView() {
        items.add(loadingItem)
    }

    private fun isThereItemsToLoad() = items.size < totalItemCount

}
