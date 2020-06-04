package `in`.ponshere.ytdemoapp.repository

import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlistdetails.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.utils.NetworkState
import `in`.ponshere.ytdemoapp.utils.TestCoroutineRule
import `in`.ponshere.ytdemoapp.utils.any
import `in`.ponshere.ytdemoapp.utils.eq
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

val mockPlaylist = mutableListOf<YTPlaylist>().apply {
    add(YTPlaylist("1", "playlist1", 10, "icon_url1"))
    add(YTPlaylist("2", "playlist2", 8, "icon_url2"))
    add(YTPlaylist("3", "playlist3", 5, "icon_url3"))
}
val mockPlaylistResultWithToken = YTPlaylistsResult(mockPlaylist, "A_TOKEN")
private const val A_PAGE_TOKEN: String = "a_page_token"

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class YTRepositoryTest {

    @Mock
    lateinit var localDataSource: YTLocalDataSource
    @Mock
    lateinit var remoteDataSource: YTRemoteDataSource
    @Mock
    lateinit var networkState: NetworkState

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @InjectMocks
    private lateinit var repository: YTRepository

    @Before
    fun setup() {
        `when`(networkState.isConnected).thenReturn(true)
    }

    @Test
    fun `should add playlist result to local data source if it is not already cached` () = testCoroutineRule.runBlocking {
        `when`(remoteDataSource.getPlaylists(anyString())).thenReturn(mockPlaylistResultWithToken)
        `when`(localDataSource.isPlaylistAlreadyCached(anyString())).thenReturn(false)

        repository.getPlaylists(A_PAGE_TOKEN)

        verify(localDataSource).addPlaylistResult(eq(mockPlaylistResultWithToken), eq(A_PAGE_TOKEN))
    }

    @Test
    fun `should not add playlist result to local data source if it is already cached` () = testCoroutineRule.runBlocking {
        `when`(remoteDataSource.getPlaylists(anyString())).thenReturn(mockPlaylistResultWithToken)
        `when`(localDataSource.isPlaylistAlreadyCached(anyString())).thenReturn(true)

        repository.getPlaylists(A_PAGE_TOKEN)

        verify(localDataSource,never()).addPlaylistResult(any(), eq(A_PAGE_TOKEN))
    }

    @Test
    fun `should recreate play list cache when the first network call is made` () = testCoroutineRule.runBlocking {
        `when`(remoteDataSource.getPlaylists(eq(null))).thenReturn(mockPlaylistResultWithToken)

        repository.getPlaylists()

        verify(localDataSource).deletePlaylistResults()
        verify(localDataSource).addPlaylistResult(eq(mockPlaylistResultWithToken), eq(null))
    }

    @Test
    fun `should get data from local data source if the device is offline` () = testCoroutineRule.runBlocking {
        `when`(localDataSource.getPlaylists(eq(null))).thenReturn(mockPlaylistResultWithToken)
        `when`(networkState.isConnected).thenReturn(false)

        repository.getPlaylists()

        verify(localDataSource).getPlaylists(eq(null))
    }
}