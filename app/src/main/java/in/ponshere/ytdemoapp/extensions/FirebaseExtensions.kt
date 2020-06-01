package `in`.ponshere.ytdemoapp.extensions

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val TAG: String = "FirebaseExtensions"

suspend fun FirebaseAuth.signInWithToken(idToken: String, activity: Activity): FirebaseUser? {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    return suspendCoroutine { cont ->
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = Firebase.auth.currentUser
                    cont.resume(user!!)
                } else {
                    // If sign in fails, display a message to the user.
                    cont.resume(null)
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

}