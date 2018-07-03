package pl.hypeapp.dataproviders.datasource

import pl.hypeapp.dataproviders.cache.CacheProviders
import pl.hypeapp.dataproviders.cache.EvictCache
import pl.hypeapp.dataproviders.service.api.ApiService
import pl.hypeapp.dataproviders.service.room.RoomService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataFactory @Inject constructor(private val apiService: ApiService,
                                      private val cacheProviders: CacheProviders,
                                      private val evictCache: EvictCache,
                                      private val roomService: RoomService) {

    fun createTvShowDataSource(): TvShowDataStore = TvShowDataSource(apiService, cacheProviders, evictCache)

    fun createWatchedShowDataSource(): WatchedShowDataStore = WatchedShowDataSource(roomService)

    fun createUserStatsDataSource(): UserStatsDataStore = UserStatsDataSource(roomService)

    fun createSeasonTrackerDataSource(): SeasonTrackerDataStore = SeasonTrackerDataSource(roomService)

    fun createEpisodeReminderDataSource(): EpisodeReminderDataStore = EpisodeReminderDataSource(roomService)

    fun createPremiereReminderDataSource(): PremiereReminderDataStore = PremiereReminderDataSource(roomService)

}
