package `in`.ponshere.ytdemoapp.repository

import `in`.ponshere.ytdemoapp.BaseTest
import `in`.ponshere.ytdemoapp.CacheRetrievalPolicy
import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylist
import `in`.ponshere.ytdemoapp.playlist.models.YTPlaylistsResult
import `in`.ponshere.ytdemoapp.utils.NetworkState
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

val mockPlaylist = mutableListOf<YTPlaylist>().apply {
    add(YTPlaylist("1", "playlist1", 10, "icon_url1"))
    add(YTPlaylist("2", "playlist2", 8, "icon_url2"))
    add(YTPlaylist("3", "playlist3", 5, "icon_url3"))
}
val mockPlaylistResultWithToken = YTPlaylistsResult(mockPlaylist, "A_TOKEN")
private const val A_PAGE_TOKEN: String = "a_page_token"
const val A_PLAYLIST_ID = "a_playlist_id"


class YTRepositoryTest: BaseTest() {

    @Mock
    lateinit var localDataSource: YTLocalDataSource

    @Mock
    lateinit var remoteDataSource: YTRemoteDataSource

    @Mock
    lateinit var networkState: NetworkState

    @InjectMocks
    private lateinit var repository: YTRepository

    @Before
    fun setup() {
        `when`(networkState.isConnected).thenReturn(true)
    }

    @Test
    fun `should add playlist result to local data source if it is not already cached`() = testCoroutineRule.runBlocking {
        `when`(remoteDataSource.getPlaylists(anyString())).thenReturn(mockPlaylistResultWithToken)
        `when`(localDataSource.isPlaylistAlreadyCached(anyString())).thenReturn(false)

        repository.getPlaylists(A_PAGE_TOKEN)

        verify(localDataSource).addPlaylistResult(mockPlaylistResultWithToken, A_PAGE_TOKEN)
    }

    @Test
    fun `should not add playlist result to local data source if it is already cached`() = testCoroutineRule.runBlocking {
        `when`(remoteDataSource.getPlaylists(anyString())).thenReturn(mockPlaylistResultWithToken)
        `when`(localDataSource.isPlaylistAlreadyCached(anyString())).thenReturn(true)

        repository.getPlaylists(A_PAGE_TOKEN)

        verify(localDataSource).isPlaylistAlreadyCached(ArgumentMatchers.anyString())
        verifyNoMoreInteractions(localDataSource)
    }

    @Test
    fun `should recreate play list cache when the first network call is made`() = testCoroutineRule.runBlocking {
        `when`(remoteDataSource.getPlaylists(eq(null))).thenReturn(mockPlaylistResultWithToken)

        repository.getPlaylists()

        verify(localDataSource).deletePlaylistResults()
        verify(localDataSource).addPlaylistResult(mockPlaylistResultWithToken, null)
    }

    @Test
    fun `should get data from local data source if the device is offline`() = testCoroutineRule.runBlocking {
        `when`(localDataSource.getPlaylists(eq(null))).thenReturn(mockPlaylistResultWithToken)
        `when`(networkState.isConnected).thenReturn(false)

        repository.getPlaylists()

        verify(localDataSource).getPlaylists(null)
    }

    @Test
    fun `should give high preference to local data source WHEN cache retrieval policy is cache_first even if device is online`() = testCoroutineRule.runBlocking {
        `when`(networkState.isConnected).thenReturn(true)
        `when`(localDataSource.isNextPlaylistVideosDataAvailable(A_PLAYLIST_ID, A_PAGE_TOKEN)).thenReturn(true)
//        `when`(localDataSource.isPlaylistVideosAlreadyCached(A_PLAYLIST_ID, A_PAGE_TOKEN)).thenReturn(true)

        repository.getPlaylistVideos(A_PLAYLIST_ID, A_PAGE_TOKEN, CacheRetrievalPolicy.CACHE_FIRST)

        verify(localDataSource).getPlaylistVideos(A_PLAYLIST_ID, A_PAGE_TOKEN, CacheRetrievalPolicy.CACHE_FIRST)
    }
}