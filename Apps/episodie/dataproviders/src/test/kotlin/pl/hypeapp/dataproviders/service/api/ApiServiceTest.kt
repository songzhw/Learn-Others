package pl.hypeapp.dataproviders.service.api

import org.amshove.kluent.mock
import org.junit.Before
import org.junit.Ignore

@Ignore
class ApiServiceTest {

    private lateinit var apiService: ApiService

    private val episodieApi: EpisodieApi = mock()

    @Before
    fun setUp() {
        apiService = ApiService(episodieApi)
    }

//    @Test
//    fun `should get most popular`() {
//        val mostPopularEntity: Single<MostPopularEntity> = mock()
//        given(episodieApi.getMostPopular(any(), any())).willReturn(mostPopularEntity)
//
//        val response = apiService.getMostPopular(any(), any())
//
//        verify(episodieApi).getMostPopular(any(), any())
//        mostPopularEntity `should equal` response
//    }
//
//    @Test
//    fun `should get top list`() {
//        val topListEntity: Single<TopListEntity> = mock()
//        given(episodieApi.getTopList(any(), any())).willReturn(topListEntity)
//
//        val response = apiService.getTopList(any(), any())
//
//        verify(episodieApi).getTopList(any(), any())
//        topListEntity `should equal` response
//    }
//
//    @Test
//    fun `should get all seasons`() {
//        val id = "12"
//        val allSeasonsEntity: Single<AllSeasonsEntity> = mock()
//        given(episodieApi.getAllSeasons(id)).willReturn(allSeasonsEntity)
//
//        val response = apiService.getAllSeasons(id)
//
//        verify(episodieApi).getAllSeasons(id)
//        allSeasonsEntity `should equal` response
//    }

}
