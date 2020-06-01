package `in`.ponshere.ytdemoapp.extensions

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG: String = "FirebaseExtensions"

suspend fun FirebaseAuth.signInWithToken(idToken: String, activity: Activity): FirebaseUser? {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    return suspendCoroutine { cont ->
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    Timber.i("Google SignIn successful")
                    val user = Firebase.auth.currentUser
                    cont.resume(user!!)
                } else {
                    Timber.e(task.exception, "Google SignIn failed")
                    cont.resume(null)
                }
            }
    }

}