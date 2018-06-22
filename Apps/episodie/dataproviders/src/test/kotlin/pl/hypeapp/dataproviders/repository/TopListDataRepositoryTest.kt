package pl.hypeapp.dataproviders.repository

import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Test
import pl.hypeapp.dataproviders.datasource.DataFactory
import pl.hypeapp.dataproviders.datasource.TvShowDataStore
import pl.hypeapp.dataproviders.entity.api.TopListEntity
import pl.hypeapp.dataproviders.entity.mapper.tvshow.TopListEntityMapper
import pl.hypeapp.domain.model.PageableRequest

class TopListDataRepositoryTest {

    private lateinit var topListDataRepository: TopListDataRepository

    private val dataFactory: DataFactory = mock()

    private val topListEntityMapper: TopListEntityMapper = mock()

    private val tvShowDataStore: TvShowDataStore = mock()

    @Before
    fun setUp() {
        topListDataRepository = TopListDataRepository(dataFactory, topListEntityMapper)
        given(dataFactory.createTvShowDataSource()).willReturn(tvShowDataStore)
    }

    @Test
    fun `shouldGetMostPopular`() {
        val topListEntity: TopListEntity = mock()
        val pageableRequest: PageableRequest = mock()
        val update = false
        given(tvShowDataStore.getTopList(pageableRequest, update)).willReturn(Single.just(topListEntity))

        topListDataRepository.getTopList(pageableRequest, update)

        verify(dataFactory).createTvShowDataSource()
        verify(tvShowDataStore).getTopList(pageableRequest, update)
    }

}
