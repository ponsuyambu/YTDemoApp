package `in`.ponshere.ytdemoapp.playlist.repository

import `in`.ponshere.ytdemoapp.datasource.YTLocalDataSource
import `in`.ponshere.ytdemoapp.datasource.YTRemoteDataSource
import `in`.ponshere.ytdemoapp.playlist.viewmodels.mockPlaylistResultWithToken
import `in`.ponshere.ytdemoapp.utils.TestCoroutineRule
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
import org.mockito.Mockito
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
    fun `should add playlist result if it is not already cached` () = testCoroutineRule.runBlocking {
        Mockito.`when`(remoteDataSource.getPlaylists(Mockito.anyString())).thenReturn(mockPlaylistResultWithToken)
        Mockito.`when`(localDataSource.isAlreadyCached(Mockito.anyString())).thenReturn(false)

        repository.getPlaylists(A_PAGE_TOKEN)

        Mockito.verify(localDataSource).addPlaylistResult(eq(mockPlaylistResultWithToken))
    }
}