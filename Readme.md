# YTDemo App
Demo implemenation of YouTube app using YouTube data API.

## Authentication
App uses the Google authentication to authenticate the user. YouTube data API expects at least a single channel for the current user. If the user does not have a channel, it throws 404 error. Hence it is manadatory that user should have created a channel in his account to use the app.

## Tech Stack
- Kotlin
- Kotlin Coroutines
- MVVM
- Android Architecture componenets
    - ViewModel
    - Live Data
- Room
- Dagger

## Screens
### 1. Authentication screen
The application offers user Google sign in. Using Google SignIn, the user can sign-in to the app and proceed to the main screen.
### 2. Playlist screen
This screen shows the list of playlists from the currently logged in user's channel.

## Notes:
- To build the release variant of the app locally(for the reviewer), release keystore is stored in git. Keystore has to be kept safely in other secure place or else app siging by Goolge play can be used.

## References
### YouTube APIs
- https://developers.google.com/youtube/v3/quickstart/android
- https://developers.google.com/youtube/v3/docs/playlists/list
### Google SignIn
- https://developers.google.com/identity/protocols/oauth2
- Scopes - https://developers.google.com/android/reference/com/google/android/gms/common/Scopes

### Testing
- https://developer.android.com/training/testing/set-up-project