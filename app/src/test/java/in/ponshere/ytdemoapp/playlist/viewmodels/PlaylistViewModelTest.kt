package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.repository.YTRepository
import `in`.ponshere.ytdemoapp.utils.TestCoroutineRule
import `in`.ponshere.ytdemoapp.utils.any
import `in`.ponshere.ytdemoapp.utils.assertTrue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlaylistViewModelTest {
    @Mock
    private lateinit var repository: YTRepository

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

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
    }

    @Test
    fun `should show progress and fetch playlist when requested for the first page`() = testCoroutineRule.runBlocking {
        val mockPlaylist = mock(YTPlaylist::class.java)
        val playlistResult = YTPlaylistsResult(mutableListOf<YTPlaylist>().apply { add(mockPlaylist) }, "next")
        `when`(repository.isNextPlaylistDataAvailable(null)).thenReturn(true)
        `when`(repository.getPlaylists(null)).thenReturn(playlistResult)

        viewModel.fetchPlaylist()

        assertNull(viewModel.status().value)
        val playLists = viewModel.playlists().value
        Assert.assertNotNull(playLists)
        assertTrue(playLists?.size?.equals(1))
        assertTrue(playLists?.get(0)?.equals(mockPlaylist))
    }
}
