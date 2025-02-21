package com.yandex.practicum.middle_homework_5.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yandex.practicum.middle_homework_5.data.database.entity.News

@Dao
interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<News>)

    @Query("Select * From news Order By page")
    fun getNews(): PagingSource<Int, News>

    @Query("Delete From news")
    suspend fun clearAllNews()
}

