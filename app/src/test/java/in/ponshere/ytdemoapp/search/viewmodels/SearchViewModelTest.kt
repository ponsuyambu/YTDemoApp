package `in`.ponshere.ytdemoapp.search.viewmodels

import `in`.ponshere.ytdemoapp.BaseTest
import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.datasource.FIRST_PAGE_TOKEN
import `in`.ponshere.ytdemoapp.repository.YTRepository
import `in`.ponshere.ytdemoapp.utils.assertTrue
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*

class SearchViewModelTest : BaseTest() {

    @Mock
    private lateinit var repository: YTRepository

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        viewModel = SearchViewModel(repository)
    }

    @Test
    fun `should not fetch search videos when next search video data is not available`() = testCoroutineRule.runBlocking {
        `when`(repository.isNextPlaylistDataAvailable(FIRST_PAGE_TOKEN)).thenReturn(false)

        viewModel.fetchVideosFor("searchTerm")

        verify(repository, never()).getPlaylists(anyString())
        assertTrue(viewModel.showProgress().value?.not())
        Assert.assertNull(viewModel.status().value)
        assertTrue(viewModel.showPlayAll().value?.not())
    }

    @Test
    fun `should fetch search videos for a search term when requested`() = testCoroutineRule.runBlocking {
        val searchTerm = "hello"
        val mockVideo = mock(YTVideo::class.java)
        val videoResult = YTVideosResult(mutableListOf<YTVideo>().apply { add(mockVideo) }, "next")
        `when`(repository.isNextPlaylistDataAvailable(FIRST_PAGE_TOKEN)).thenReturn(true)
        `when`(repository.getVideosFor(searchTerm, FIRST_PAGE_TOKEN)).thenReturn(videoResult)

        viewModel.fetchVideosFor(searchTerm)

        assertTrue(viewModel.showProgress().value?.not())
        val searchVideos = viewModel.listModels().value
        assertNotNull(searchVideos)
        assertTrue(searchVideos?.size?.equals(1))
        assertTrue(searchVideos?.get(0)?.equals(mockVideo))
    }

    @Test
    fun `should not show play all when search videos are empty`() = testCoroutineRule.runBlocking {
        val searchTerm = "123"
        val videoResult = YTVideosResult(mutableListOf<YTVideo>().apply { }, "next")
        `when`(repository.isNextPlaylistDataAvailable(FIRST_PAGE_TOKEN)).thenReturn(true)
        `when`(repository.getVideosFor(searchTerm, FIRST_PAGE_TOKEN)).thenReturn(videoResult)

        viewModel.fetchVideosFor(searchTerm)

        assertTrue(viewModel.showPlayAll().value?.not())
    }
}