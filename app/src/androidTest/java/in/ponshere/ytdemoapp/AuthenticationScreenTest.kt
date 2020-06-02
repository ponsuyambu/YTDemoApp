package `in`.ponshere.ytdemoapp

import `in`.ponshere.ytdemoapp.authentcation.ui.AuthenticationScreen
import `in`.ponshere.ytdemoapp.idlingresource.SignInIdlingResource
import `in`.ponshere.ytdemoapp.playlist.ui.PlaylistScreen
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.android.gms.auth.api.signin.internal.SignInHubActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * IMPORTANT:
 *   To run this test, google sign in has to be already done on the device.
 *   Will be improved after I complete all the tasks(if time permits).
 *   As of now, it is a dependency that at least once the google sign in has to be done before running.
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class AuthenticationScreenTest {
    @get:Rule
    val activityRule = IntentsTestRule(AuthenticationScreen::class.java)
    private val singIdlingResource = SignInIdlingResource()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        IdlingRegistry.getInstance().register(singIdlingResource)
        activityRule.activity.singInIdlingResource = singIdlingResource
    }

    @Test
    fun shouldDisplaySignInButton() {
        onView(withId(R.id.btnSignIn)).check(matches(isDisplayed()));
    }

    @Test
    fun shouldNavigateToPlaylistScreen_When_AuthenticationIsSuccessful() {
        onView(withId(R.id.btnSignIn)).perform(click())
        intended(hasComponent(SignInHubActivity::class.java.name))
        intended(hasComponent(PlaylistScreen::class.java.name))
    }
}