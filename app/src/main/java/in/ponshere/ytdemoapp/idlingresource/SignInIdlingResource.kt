package `in`.ponshere.ytdemoapp.idlingresource

import androidx.test.espresso.IdlingResource

class SignInIdlingResource : IdlingResource, SignInListener {
    private var idle = true
    private var resourceCallback:
            IdlingResource.ResourceCallback? = null

    override fun registerIdleTransitionCallback(
        callback: IdlingResource.ResourceCallback?
    ) {
        resourceCallback = callback
    }

    override fun onSignCompleted() {
        idle = true
        resourceCallback?.onTransitionToIdle()
    }

    override fun onSignInStarted() {
        idle = false
    }

    override fun getName(): String {
        return SignInIdlingResource::class.java.simpleName
    }

    override fun isIdleNow() = idle
}