package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.playlist.repository.YTRepository
import `in`.ponshere.ytdemoapp.playlist.repository.models.YTPlaylist
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

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: YTRepository

    lateinit var viewmodel: PlaylistViewModel

    @Before
    fun setup() {
        viewmodel = PlaylistViewModel(repository)
    }

    @Test
    fun `should show progress dialog when playlist request started`() {
        viewmodel.fetchPlaylist()

        assertTrue(viewmodel.showProgress().value)
    }

    @Test
    fun `should invoke playlists from repository when playlist request started`() {
        viewmodel.fetchPlaylist()

        testCoroutineRule.runBlockingTest {
            Mockito.verify(repository).getPlaylists()
        }
    }

    @Test
    fun `should update playlists when playlist received from repository`() {
        testCoroutineRule.runBlockingTest {
            val expectedPlaylist = mutableListOf<YTPlaylist>()
            Mockito.`when`(repository.getPlaylists()).thenReturn(expectedPlaylist)

            viewmodel.fetchPlaylist()

            Assert.assertEquals(expectedPlaylist, viewmodel.playlists().value)
        }
    }

}
