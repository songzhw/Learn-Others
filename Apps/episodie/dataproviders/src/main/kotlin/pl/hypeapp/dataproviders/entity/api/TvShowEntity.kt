package pl.hypeapp.dataproviders.entity.api

import com.google.gson.annotations.SerializedName

data class TvShowEntity(@SerializedName("tvShowApiId")
                        val id: String,
                        val imdbId: String,
                        val name: String,
                        val network: String,
                        val genre: String,
                        val summary: String,
                        val episodeOrder: Int,
                        val officialSite: String,
                        val status: String,
                        @SerializedName("runtime")
                        val episodeRuntime: Long,
                        val fullRuntime: Long,
                        val premiered: String,
                        val imageMedium: String,
                        val imageOriginal: String)
