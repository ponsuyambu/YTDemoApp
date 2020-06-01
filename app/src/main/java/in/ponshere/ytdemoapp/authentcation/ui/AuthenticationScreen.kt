package `in`.ponshere.ytdemoapp.authentcation.ui

import `in`.ponshere.ytdemoapp.R
import `in`.ponshere.ytdemoapp.authentcation.GoogleSingInResultContract
import `in`.ponshere.ytdemoapp.extensions.signInWithToken
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.coroutines.launch

class AuthenticationScreen : AppCompatActivity() {
    private val signIn = registerForActivityResult(GoogleSingInResultContract()) { token ->
        token?.let {
            lifecycle.coroutineScope.launch {
                val user = Firebase.auth.signInWithToken(token, this@AuthenticationScreen)
                updateUI(user)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        btnSignIn.setOnClickListener {
            signIn.launch(null)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        Toast.makeText(this, "Welcome ${user?.displayName}!", Toast.LENGTH_SHORT).show()
    }
}