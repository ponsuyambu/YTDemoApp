package `in`.ponshere.ytdemoapp.authentcation.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.authentcation.GoogleSingInResultContract
import `in`.ponshere.ytdemoapp.extensions.signInWithToken
import `in`.ponshere.ytdemoapp.idlingresource.SignInIdlingResource
import `in`.ponshere.ytdemoapp.playlist.ui.PlaylistScreen
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.coroutineScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.coroutines.launch

class AuthenticationScreen : DaggerAppCompatActivity() {

    @VisibleForTesting
    var singInIdlingResource: SignInIdlingResource? = null

    private val signIn = registerForActivityResult(GoogleSingInResultContract()) { token ->
        token?.let {
            lifecycle.coroutineScope.launch {
                val user = Firebase.auth.signInWithToken(token, this@AuthenticationScreen)
                launchPlaylistScreen()
                singInIdlingResource?.onSignCompleted()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        if(GoogleSignIn.getLastSignedInAccount(this) != null) {
            launchPlaylistScreen()
            return
        }
        btnSignIn.setOnClickListener {
            singInIdlingResource?.onSignInStarted()
            signIn.launch(null)
        }
    }

    private fun launchPlaylistScreen() {
        PlaylistScreen.launch(this@AuthenticationScreen)
        finish()
    }

}