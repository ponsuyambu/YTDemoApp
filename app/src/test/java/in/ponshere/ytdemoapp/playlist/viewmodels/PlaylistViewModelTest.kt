package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.BaseTest
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.repository.YTRepository
import `in`.ponshere.ytdemoapp.utils.assertTrue
import org.junit.Assert
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

class PlaylistViewModelTest : BaseTest() {
    @Mock
    private lateinit var repository: YTRepository

    private lateinit var viewModel: PlaylistViewModel

    @Before
    fun setup() {
        viewModel = PlaylistViewModel(repository)
    }

    @Test
    fun `should not fetch playlist when next playlist data is not available`() = testCoroutineRule.runBlocking {
        `when`(repository.isNextPlaylistDataAvailable(null)).thenReturn(false)

        viewModel.fetchPlaylist()

        verify(repository, never()).getPlaylists(any())
        assertTrue(viewModel.showProgress().value?.not())
        assertNull(viewModel.status().value)
    }

    @Test
    fun `should show progress and fetch playlist when requested for the first page`() = testCoroutineRule.runBlocking {
        val mockPlaylist = mock(YTPlaylist::class.java)
        val playlistResult = YTPlaylistsResult(mutableListOf<YTPlaylist>().apply { add(mockPlaylist) }, "next")
        `when`(repository.isNextPlaylistDataAvailable(null)).thenReturn(true)
        `when`(repository.getPlaylists(null)).thenReturn(playlistResult)

        viewModel.fetchPlaylist()

        assertNull(viewModel.status().value)
        val playLists = viewModel.listModels().value
        Assert.assertNotNull(playLists)
        assertTrue(playLists?.size?.equals(1))
        assertTrue(playLists?.get(0)?.equals(mockPlaylist))
    }
}
