package `in`.ponshere.ytdemoapp.playlist.viewmodels

import `in`.ponshere.ytdemoapp.playlist.repository.YTRepository
import `in`.ponshere.ytdemoapp.utils.assertTrue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PlaylistViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

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

}
