package `in`.ponshere.ytdemoapp.playlist.ui

import `in`.ponshere.ytdemoapp.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*


class PlaylistScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_screen)

        val credential = GoogleAccountCredential.usingOAuth2(
            this,
            Collections.singleton("https://www.googleapis.com/auth/youtube")
        )
        credential.selectedAccount = GoogleSignIn.getLastSignedInAccount(this)?.account
        val youTube = YouTube.Builder(
            NetHttpTransport(),
            JacksonFactory(),
            credential
        )
            .setApplicationName("YTDemoApp")
            .build()

        val task = youTube.playlists()?.list("snippet,contentDetails")
        task?.mine = true
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            val result = task?.execute()
            result?.items?.forEach { playlist ->
                Timber.d("Playlist: ${playlist?.snippet?.title}")
            }
        }
    }

    companion object {
        fun launch(caller: Activity) {
            val intent = Intent(caller, PlaylistScreen::class.java)
            caller.startActivity(intent)
        }
    }
}