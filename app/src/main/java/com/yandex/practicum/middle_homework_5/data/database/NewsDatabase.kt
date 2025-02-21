package com.yandex.practicum.middle_homework_5.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yandex.practicum.middle_homework_5.data.database.entity.News
import com.yandex.practicum.middle_homework_5.data.database.entity.RemoteKeys

@Database(
    entities = [News::class, RemoteKeys::class],
    version = 1,
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}