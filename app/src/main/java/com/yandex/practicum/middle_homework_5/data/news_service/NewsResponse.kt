package com.yandex.practicum.middle_homework_5.data.news_service

import com.yandex.practicum.middle_homework_5.data.database.entity.News

data class NewsResponse(
    val nextPage: Int?,
    val news: List<News>
)