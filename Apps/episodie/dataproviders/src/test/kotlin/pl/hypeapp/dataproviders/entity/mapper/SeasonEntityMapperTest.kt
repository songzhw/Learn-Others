package pl.hypeapp.dataproviders.entity.mapper

import com.nhaarman.mockito_kotlin.mock
import org.amshove.kluent.`should equal`
import org.junit.Before
import org.junit.Test
import pl.hypeapp.dataproviders.entity.api.SeasonEntity
import pl.hypeapp.dataproviders.entity.mapper.tvshow.EpisodeEntityMapper
import pl.hypeapp.dataproviders.entity.mapper.tvshow.SeasonEntityMapper

class SeasonEntityMapperTest {

    private lateinit var seasonEntityMapper: SeasonEntityMapper

    private val episodeEntityMapper: EpisodeEntityMapper = mock()

    private val entity: SeasonEntity = mock()

    @Before
    fun setUp() {
        seasonEntityMapper = SeasonEntityMapper(episodeEntityMapper)
    }

    @Test
    fun `should transform entity to model`() {
        val model = seasonEntityMapper.transform(entity)

        model?.episodes `should equal` entity.episodes
        model?.seasonNumber `should equal` entity.seasonNumber
        model?.episodeOrder `should equal` entity.episodes.size
        model?.imageMedium `should equal` entity.imageMedium
        model?.premiereDate `should equal` entity.premiereDate
        model?.fullRuntime `should equal` entity.runtime
        model?.seasonId `should equal` entity.seasonId
    }

}
