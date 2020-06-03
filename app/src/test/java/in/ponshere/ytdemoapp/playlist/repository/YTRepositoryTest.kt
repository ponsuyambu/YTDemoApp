package `in`.ponshere.ytdemoapp.playlist.repository

import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.viewmodels.mockPlaylistResultWithToken
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

private const val A_PAGE_TOKEN: String = "a_page_token"

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class YTRepositoryTest {

    @Mock
    lateinit var localDataSource: YTLocalDataSource
    @Mock
    lateinit var remoteDataSource: YTRemoteDataSource

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @InjectMocks
    private lateinit var repository: YTRepository

    @Before
    fun setUp() {

    }

    @Test
    fun `should add playlist result to local data source if it is not already cached` () = testCoroutineRule.runBlocking {
        `when`(remoteDataSource.getPlaylists(anyString())).thenReturn(mockPlaylistResultWithToken)
        `when`(localDataSource.isAlreadyCached(anyString())).thenReturn(false)

        repository.getPlaylists(A_PAGE_TOKEN)

        verify(localDataSource).addPlaylistResult(eq(mockPlaylistResultWithToken))
    }

    @Test
    fun `should not add playlist result to local data source if it is already cached` () = testCoroutineRule.runBlocking {
        `when`(remoteDataSource.getPlaylists(anyString())).thenReturn(mockPlaylistResultWithToken)
        `when`(localDataSource.isAlreadyCached(anyString())).thenReturn(true)

        repository.getPlaylists(A_PAGE_TOKEN)

        verify(localDataSource,never()).addPlaylistResult(any())
    }

}