package `in`.ponshere.ytdemoapp.authentcation.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.authentcation.GoogleSingInResultContract
import `in`.ponshere.ytdemoapp.extensions.signInWithToken
import `in`.ponshere.ytdemoapp.idlingresource.SignInIdlingResource
import `in`.ponshere.ytdemoapp.playlist.ui.PlaylistScreen
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.coroutines.launch

class AuthenticationScreen : AppCompatActivity() {

    @VisibleForTesting
    var singInIdlingResource: SignInIdlingResource? = null

    private val signIn = registerForActivityResult(GoogleSingInResultContract()) { token ->
        token?.let {
            lifecycle.coroutineScope.launch {
                val user = Firebase.auth.signInWithToken(token, this@AuthenticationScreen)
                PlaylistScreen.launch(this@AuthenticationScreen)
                singInIdlingResource?.onSignCompleted()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        btnSignIn.setOnClickListener {
            singInIdlingResource?.onSignInStarted()
            signIn.launch(null)
        }
    }

}