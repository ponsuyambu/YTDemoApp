package `in`.ponshere.ytdemoapp.playlistdetails.viewmodels

import `in`.ponshere.ytdemoapp.BaseTest
import `in`.ponshere.ytdemoapp.common.models.YTVideo
import `in`.ponshere.ytdemoapp.common.models.YTVideosResult
import `in`.ponshere.ytdemoapp.repository.YTRepository
import `in`.ponshere.ytdemoapp.utils.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*

class PlaylistDetailsViewModelTest : BaseTest() {

    @Mock
    private lateinit var repository: YTRepository

    private lateinit var viewModel: PlaylistDetailsViewModel

    @Before
    fun setup() {
        viewModel = PlaylistDetailsViewModel(repository)
    }

    @Test
    fun `should not fetch playlist videos when next playlist video data is not available`() =
        testCoroutineRule.runBlocking {
            val playlistId = "123"
            `when`(repository.isNextPlaylistDataAvailable(null)).thenReturn(false)

            viewModel.fetchPlaylistVideos(playlistId)

            verify(repository, never()).getPlaylists(anyString())
            assertTrue(viewModel.showProgress().value?.not())
            assertNull(viewModel.status().value)
            assertTrue(viewModel.showPlayAll().value?.not())
        }

    @Test
    fun `should fetch playlist videos when requested`() = testCoroutineRule.runBlocking {
        val playlistId = "123"
        val mockVideo = mock(YTVideo::class.java)
        val videoResult = YTVideosResult(mutableListOf<YTVideo>().apply { add(mockVideo) }, "next")
        `when`(repository.isNextPlaylistDataAvailable(null)).thenReturn(true)
        `when`(repository.getPlaylistVideos(playlistId, null)).thenReturn(videoResult)

        viewModel.fetchPlaylistVideos(playlistId)

        assertTrue(viewModel.showProgress().value?.not())
        assertTrue(viewModel.showPlayAll().value)
        val playListVideos = viewModel.listModels().value
        assertNotNull(playListVideos)
        assertTrue(playListVideos?.size?.equals(1))
        assertTrue(playListVideos?.get(0)?.equals(mockVideo))
    }

    @Test
    fun `should not show play all when playlist videos are empty`() = testCoroutineRule.runBlocking {
        val playlistId = "123"
        val videoResult = YTVideosResult(mutableListOf<YTVideo>().apply { }, "next")
        `when`(repository.isNextPlaylistDataAvailable(null)).thenReturn(true)
        `when`(repository.getPlaylistVideos(playlistId, null)).thenReturn(videoResult)

        viewModel.fetchPlaylistVideos(playlistId)

        assertTrue(viewModel.showPlayAll().value?.not())
    }
}