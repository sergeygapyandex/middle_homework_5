package com.yandex.practicum.middle_homework_5.di

import com.yandex.practicum.middle_homework_5.data.SourceProvider
import com.yandex.practicum.middle_homework_5.data.database.NewsDatabase
import com.yandex.practicum.middle_homework_5.data.work_manager.WorkManagerServiceImp
import com.yandex.practicum.middle_homework_5.ui.AppViewModel
import com.yandex.practicum.middle_homework_5.ui.contract.NewsService
import com.yandex.practicum.middle_homework_5.ui.contract.WorkManagerService
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single<NewsService> { SourceProvider.provideNewsService() }
    single<NewsDatabase> { SourceProvider.provideNewsDatabase() }
    single<WorkManagerService> { WorkManagerServiceImp(androidApplication(), get()) }
    viewModel { AppViewModel(get(), get(), get()) }
}