package pl.hypeapp.dataproviders.service.api

import io.reactivex.Single
import pl.hypeapp.dataproviders.entity.api.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiService @Inject constructor(private val episodieApi: EpisodieApi) : EpisodieApi {

    override fun getMostPopular(page: Int, size: Int): Single<MostPopularEntity> {
        return episodieApi.getMostPopular(page, size)
    }

    override fun getTopList(page: Int, size: Int): Single<TopListEntity> {
        return episodieApi.getTopList(page, size)
    }

    override fun search(query: String): Single<List<TvShowEntity>> {
        return episodieApi.search(query)
    }

    override fun basicSearch(query: String): Single<List<BasicSearchResultEntity>> {
        return episodieApi.basicSearch(query)
    }

    override fun getTvShow(tvShowId: String): Single<TvShowEntity> {
        return episodieApi.getTvShow(tvShowId)
    }

    override fun getAllSeasons(tvShowId: String, afterPremiereDate: Boolean): Single<AllSeasonsEntity> {
        return episodieApi.getAllSeasons(tvShowId, afterPremiereDate)
    }

    override fun getPremiereDates(page: Int, size: Int, fromDate: String): Single<PageablePremiereDates> {
        return episodieApi.getPremiereDates(page, size, fromDate)
    }
}
