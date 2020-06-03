package `in`.ponshere.ytdemoapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [YTPlaylistEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistResultDao(): YTPlaylistResultDao
}