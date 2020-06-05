package `in`.ponshere.ytdemoapp

import `in`.ponshere.ytdemoapp.authentication.ui.AuthenticationScreen
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
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

    @Test
    fun shouldDisplaySignInButton() {
        onView(withId(R.id.btnSignIn)).check(matches(isDisplayed()));
    }
}