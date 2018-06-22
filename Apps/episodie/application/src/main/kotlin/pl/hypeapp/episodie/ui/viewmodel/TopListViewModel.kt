package pl.hypeapp.episodie.ui.viewmodel

import android.arch.lifecycle.ViewModel
import pl.hypeapp.domain.model.collections.TopListModel
import pl.hypeapp.domain.model.tvshow.TvShowModel

class TopListViewModel : ViewModel() {

    var page: Int = 0

    var retainedModel: TopListModel? = null

    var tvShowList: ArrayList<TvShowViewModel> = ArrayList()

    fun retainModel(model: TopListModel) {
        retainedModel = model
        tvShowList.addAll(model.tvShows.map { TvShowViewModel(it) })
        page = model.pageableRequest.page
    }

    fun clearModel() {
        retainedModel = null
        tvShowList.clear()
        page = 0
    }

    fun clearAndRetainModel(model: TopListModel) {
        clearModel()
        retainModel(model)
    }

    fun updateModel(tvShows: List<TvShowModel>) {
        retainedModel?.tvShows = tvShows
        tvShowList.clear()
        tvShowList.addAll(tvShows.map { TvShowViewModel(it) })
    }

    inline fun loadModel(requestMostPopular: () -> Unit, populateRecyclerList: () -> Unit) {
        if (retainedModel == null)
            requestMostPopular()
        else
            populateRecyclerList()
    }

}
