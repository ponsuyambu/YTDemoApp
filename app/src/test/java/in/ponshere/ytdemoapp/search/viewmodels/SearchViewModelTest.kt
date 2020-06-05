package `in`.ponshere.ytdemoapp.search.viewmodels

import `in`.ponshere.ytdemoapp.BaseTest
import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.playlist.models.YTVideosResult
import `in`.ponshere.ytdemoapp.repository.YTRepository
import `in`.ponshere.ytdemoapp.utils.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock

class SearchViewModelTest : BaseTest() {

    @Mock
    private lateinit var repository: YTRepository

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        viewModel = SearchViewModel(repository)
    }

    @Test
    fun `should fetch search videos for a search term when requested`() = testCoroutineRule.runBlocking {
        val searchTerm = "hello"
        val mockVideo = mock(YTVideo::class.java)
        val videoResult = YTVideosResult(mutableListOf<YTVideo>().apply { add(mockVideo) }, "next")
        Mockito.`when`(repository.getVideosFor(searchTerm, null)).thenReturn(videoResult)

        viewModel.fetchVideosFor(searchTerm)

        assertTrue(viewModel.showProgress().value)
        val searchVideos = viewModel.searchVideos().value
        assertNotNull(searchVideos)
        assertTrue(searchVideos?.size?.equals(1))
        assertTrue(searchVideos?.get(0)?.equals(mockVideo))
    }
}