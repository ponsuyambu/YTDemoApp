package `in`.ponshere.ytdemoapp.db

import `in`.ponshere.ytdemoapp.db.playlist.YTPlaylistDao
import `in`.ponshere.ytdemoapp.db.playlist.YTPlaylistEntity
import `in`.ponshere.ytdemoapp.db.playlistdetails.YTPlaylistDetailsEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [YTPlaylistEntity::class, YTPlaylistDetailsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistResultDao(): YTPlaylistDao
}