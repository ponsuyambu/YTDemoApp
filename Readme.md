# YTDemo App
Demo implemenation of YouTube app using YouTube data API.

## Tech Stack
- Kotlin
- Kotlin Coroutines
- MVVM
- Android Architecture componenets
    - ViewModel
    - Live Data
- Android Jetpack libraries
- Room
- Dagger
- Material components
- Timber
- Picasso
- Gson

Just make sure you select the release variant in Android Studio to build the app. You can also download the apk directly from [here](app-release.apk).

## Screens
### 1. Authentication screen
The application offers user Google sign in. Using Google SignIn, the user can sign-in to the app and proceed to the main screen.
#### Authentication
App uses the Google authentication to authenticate the user. YouTube data API expects at least a single channel for the current user. If the user does not have a channel, it throws 404 error. Hence it is manadatory that user should have created a channel in his account to use the app.

### 2. Playlist screen
This screen shows the list of playlists from the currently logged in user's channel.

### 3. Playlist Details screen
Shows the details of the playlists. User can play a single video from the list or all the videos can be played together.

### 4. Search screen
From the play list screen, user can navigate to the search screen. With a search term, the search can be performed

### 5. Video Player screen
Plays the videos selected from search & play list videos screen. It uses the `[android-youtube-player](https://github.com/PierfrancescoSoffritti/android-youtube-player)` which uses the `Youtube's iFrame player` internally.

## Caching
App caches the playlists and its videos locally in database. Though the search feature of the app always directly communicates with the server.

## Testing
Major logics are covered in unit tests. Due the time limit, not the all cases are covered.

## Notes:
- To build the release variant of the app locally(for the reviewer), release keystore is stored in git. Keystore has to be kept safely in other secure place or else app siging by Goolge play can be used.

## References
### YouTube APIs
- https://developers.google.com/youtube/v3/quickstart/android
- https://developers.google.com/youtube/v3/docs/playlists/list
### Google SignIn
- https://developers.google.com/identity/protocols/oauth2
- Scopes - https://developers.google.com/android/reference/com/google/android/gms/common/Scopes

