package `in`.ponshere.ytdemoapp.playlistdetails.viewmodels

import `in`.ponshere.ytdemoapp.BaseTest
import `in`.ponshere.ytdemoapp.playlist.models.YTVideosResult
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTVideo
import `in`.ponshere.ytdemoapp.repository.YTRepository
import `in`.ponshere.ytdemoapp.utils.assertTrue
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock

class PlaylistDetailsViewModelTest: BaseTest() {

    @Mock
    private lateinit var repository: YTRepository

    private lateinit var viewModel: PlaylistDetailsViewModel

    @Before
    fun setup() {
        viewModel = PlaylistDetailsViewModel(repository)
    }

    @Test
    fun `should fetch playlist videos when requested`() = testCoroutineRule.runBlocking {
            val playlistId = "123"
            val mockVideo = mock(YTVideo::class.java)
            val videoResult = YTVideosResult(mutableListOf<YTVideo>().apply { add(mockVideo) }, "next")
            Mockito.`when`(repository.getPlaylistVideos(playlistId, null)).thenReturn(videoResult)

            viewModel.fetchPlaylistVideos(playlistId)

            assertTrue(viewModel.showProgress().value)
            val playListVideos = viewModel.playlistVideos().value
            assertNotNull(playListVideos)
            assertTrue(playListVideos?.size?.equals(1))
            assertTrue(playListVideos?.get(0)?.equals(mockVideo))
        }
}