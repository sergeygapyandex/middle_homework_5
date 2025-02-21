package com.yandex.practicum.middle_homework_5.data

import android.app.Application
import androidx.paging.compose.LazyPagingItems
import androidx.room.Room
import com.yandex.practicum.middle_homework_5.data.database.NewsDatabase
import com.yandex.practicum.middle_homework_5.data.database.entity.News
import com.yandex.practicum.middle_homework_5.ui.contract.NewsService
import com.yandex.practicum.middle_homework_5.data.news_service.NewsServiceImpl

object SourceProvider {
    private const val DATABASE_NAME = "news_database"
    private const val ASSETS_FILE = "runews.json"
    var pagingItems: LazyPagingItems<News>? = null

    private var application: Application? = null
    fun init(application: Application) {
        SourceProvider.application = application
    }

    fun provideNewsService(): NewsService = NewsServiceImpl(
        application = application!!,
        fileName = ASSETS_FILE
    )
    fun provideNewsDatabase():NewsDatabase {
        return  Room.databaseBuilder(application!!, NewsDatabase::class.java, DATABASE_NAME)
            .build()
    }
}
