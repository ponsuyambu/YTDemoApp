package `in`.ponshere.ytdemoapp.db

import `in`.ponshere.ytdemoapp.db.playlist.YTPlaylistDao
import `in`.ponshere.ytdemoapp.db.playlist.YTPlaylistEntity
import `in`.ponshere.ytdemoapp.db.playlistdetails.YTPlaylistVideosDao
import `in`.ponshere.ytdemoapp.db.playlistdetails.YTPlaylistVideosEntity
import `in`.ponshere.ytdemoapp.db.videos.YTVideoInfoDao
import `in`.ponshere.ytdemoapp.db.videos.YTVideoInfoEntity
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [YTPlaylistEntity::class, YTPlaylistVideosEntity::class, YTVideoInfoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): YTPlaylistDao
    abstract fun playlistVideosDao(): YTPlaylistVideosDao
    abstract fun videoInfoDao(): YTVideoInfoDao
}