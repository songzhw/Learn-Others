package pl.hypeapp.dataproviders.service.room

import android.arch.paging.TiledDataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import pl.hypeapp.dataproviders.entity.room.WatchedEpisodeEntity
import pl.hypeapp.dataproviders.entity.room.WatchedEpisodesCountEntity
import pl.hypeapp.dataproviders.entity.room.WatchedSeasonCountEntity

@Dao
interface WatchedEpisodeDao {

    @Insert(onConflict = REPLACE)
    fun insertWatchedEpisode(watchedEpisodeEntity: WatchedEpisodeEntity)

    @Insert(onConflict = REPLACE)
    fun insertWatchedEpisodes(watchedEpisodeEntities: List<WatchedEpisodeEntity>)

    @Query("SELECT tv_show_id, COUNT(*) as count FROM watched_episodes WHERE tv_show_id = :tvShowId")
    fun getWatchedEpisodesCountByTvShowId(tvShowId: String): WatchedEpisodesCountEntity

    @Query("SELECT tv_show_id, COUNT(*) AS count FROM watched_episodes WHERE tv_show_id IN(:arg0) GROUP BY tv_show_id")
    fun getWatchedEpisodesCountByTvShowIds(tvShowIds: List<String>): List<WatchedEpisodesCountEntity>

    @Query("SELECT season_id, COUNT(*) AS count FROM watched_episodes WHERE season_id IN(:arg0) GROUP BY season_id")
    fun getWatchedEpisodesCountBySeasonIds(seasonIds: List<String>): List<WatchedSeasonCountEntity>

    @Query("SELECT episode_id FROM watched_episodes WHERE season_id = :arg0")
    fun getWatchedEpisodesIdsBySeasonId(seasonId: String): List<String>

    @Query("SELECT * FROM watched_episodes WHERE tv_show_id = :arg0")
    fun getWatchedEpisodesByTvShowId(tvShowId: String): List<WatchedEpisodeEntity>

    @Query("DELETE FROM watched_episodes WHERE tv_show_id = :arg0")
    fun deleteWatchedTvShow(tvShowId: String)

    @Query("DELETE FROM watched_episodes WHERE season_id = :arg0")
    fun deleteWatchedSeason(seasonId: String)

    @Query("DELETE FROM watched_episodes WHERE episode_id = :arg0")
    fun deleteWatchedEpisode(episodeId: String)

    @Query("SELECT COUNT(DISTINCT tv_show_id) from watched_episodes")
    fun getWatchedTvShowsSize(): Int

    @Query("SELECT * from watched_episodes")
    fun getWatchedTvShows(): TiledDataSource<WatchedEpisodeEntity>

}
