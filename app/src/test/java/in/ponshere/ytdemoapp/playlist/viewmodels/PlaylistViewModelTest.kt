package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.playlist.repository.YTRepository
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.utils.TestCoroutineRule
import `in`.ponshere.ytdemoapp.utils.assertTrue
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
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlaylistViewModelTest {

    private val mockPlaylist = mutableListOf<YTPlaylist>().apply {
        add(YTPlaylist("playlist1", 10, "icon_url1"))
        add(YTPlaylist("playlist2", 8, "icon_url2"))
        add(YTPlaylist("playlist3", 5, "icon_url3"))
    }
    private val mockPlaylistResultWithoutToken = YTPlaylistsResult(mockPlaylist, "")
    private val mockPlaylistResultWithToken = YTPlaylistsResult(mockPlaylist, "A_TOKEN")

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
            Mockito.`when`(repository.getPlaylists()).thenReturn(mockPlaylistResultWithoutToken)
        }
    }

    @Test
    fun `should show progress dialog when playlist request started`() =
        testCoroutineRule.runBlocking {
            viewModel.fetchPlaylist()

            assertTrue(viewModel.showProgress().value)
        }

    @Test
    fun `should invoke playlists from repository when playlist request started`() =
        testCoroutineRule.runBlocking {
            viewModel.fetchPlaylist()

            Mockito.verify(repository).getPlaylists()
        }

    @Test
    fun `should update playlists when playlist received from repository`() =
        testCoroutineRule.runBlocking {
            viewModel.fetchPlaylist()

            Assert.assertEquals(mockPlaylist, viewModel.playlists().value)
        }

    @Test
    fun `should send received pageToken  for subsequent playlists requests`() =
        testCoroutineRule.runBlocking {
            Mockito.`when`(repository.getPlaylists()).thenReturn(mockPlaylistResultWithToken)
            Mockito.`when`(repository.getPlaylists(Mockito.eq(mockPlaylistResultWithToken.nextPageToken)))
                .thenReturn(mockPlaylistResultWithoutToken)

            //first call
            viewModel.fetchPlaylist()
            Mockito.verify(repository).getPlaylists(Mockito.eq(null))

            //second call
            viewModel.fetchPlaylist()
            Mockito.verify(repository)
                .getPlaylists(Mockito.eq(mockPlaylistResultWithToken.nextPageToken))
        }
}
