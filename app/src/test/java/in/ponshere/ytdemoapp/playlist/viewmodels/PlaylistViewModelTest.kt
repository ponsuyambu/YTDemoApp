package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.repository.YTRepository
import `in`.ponshere.ytdemoapp.utils.TestCoroutineRule
import `in`.ponshere.ytdemoapp.utils.any
import `in`.ponshere.ytdemoapp.utils.assertTrue
import `in`.ponshere.ytdemoapp.utils.eq
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.junit.MockitoJUnitRunner

val mockPlaylist = mutableListOf<YTPlaylist>().apply {
    add(YTPlaylist("1", "playlist1", 10, "icon_url1"))
    add(YTPlaylist("2", "playlist2", 8, "icon_url2"))
    add(YTPlaylist("3", "playlist3", 5, "icon_url3"))
}
val mockPlaylistResultWithEmptyToken = YTPlaylistsResult(mockPlaylist, "")
val mockPlaylistResultWithToken = YTPlaylistsResult(mockPlaylist, "A_TOKEN")


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
        testCoroutineRule.runBlocking {
            Mockito.`when`(repository.getPlaylists()).thenReturn(mockPlaylistResultWithEmptyToken)
        }
    }

    @Test
    fun `should show progress dialog when playlist request started`() = testCoroutineRule.runBlocking {
        viewModel.fetchPlaylist()

        assertTrue(viewModel.showProgress().value)
    }

    @Test
    fun `should invoke playlists from repository when playlist request started`() = testCoroutineRule.runBlocking {
        viewModel.fetchPlaylist()

        Mockito.verify(repository).getPlaylists()
    }

    @Test
    fun `should update playlists when playlist received from repository`() = testCoroutineRule.runBlocking {
        viewModel.fetchPlaylist()

        Assert.assertEquals(mockPlaylist, viewModel.playlists().value)
    }

    @Test
    fun `should send received pageToken for subsequent playlists requests`() = testCoroutineRule.runBlocking {
        Mockito.`when`(repository.getPlaylists()).thenReturn(mockPlaylistResultWithToken)
        Mockito.`when`(repository.getPlaylists(Mockito.eq(mockPlaylistResultWithToken.nextPageToken)))
            .thenReturn(mockPlaylistResultWithEmptyToken)

        //first call
        viewModel.fetchPlaylist()
        Mockito.verify(repository).getPlaylists(Mockito.eq(null))

        //second call
        viewModel.fetchPlaylist()
        Mockito.verify(repository)
            .getPlaylists(Mockito.eq(mockPlaylistResultWithToken.nextPageToken))
    }

    @Test
    fun `should not request new playlists if the nextPageToken is empty`() = testCoroutineRule.runBlocking {
        Mockito.`when`(repository.getPlaylists(any())).thenReturn(mockPlaylistResultWithEmptyToken)

        //first call
        viewModel.fetchPlaylist()
        Mockito.verify(repository).getPlaylists(eq(null))

        //second call
        viewModel.fetchPlaylist()
        Mockito.verify(repository, never()).getPlaylists(eq(""))
    }
}
