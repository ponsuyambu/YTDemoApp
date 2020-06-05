package `in`.ponshere.ytdemoapp.authentication.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.authentication.GoogleSingInResultContract
import `in`.ponshere.ytdemoapp.extensions.signInWithToken
import `in`.ponshere.ytdemoapp.playlist.ui.PlaylistScreen
import android.os.Bundle
import androidx.lifecycle.coroutineScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.coroutines.launch

class AuthenticationScreen : DaggerAppCompatActivity() {

    private val signIn = registerForActivityResult(GoogleSingInResultContract()) { token ->
        token?.let {
            lifecycle.coroutineScope.launch {
                Firebase.auth.signInWithToken(token, this@AuthenticationScreen)
                launchPlaylistScreen()
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
            signIn.launch(null)
        }
    }

    private fun launchPlaylistScreen() {
        PlaylistScreen.launch(this@AuthenticationScreen)
        finish()
    }

}