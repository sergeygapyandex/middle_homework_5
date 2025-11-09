package com.yandex.practicum.middle_homework_5.settings.di

import com.yandex.practicum.middle_homework_5.settings.data_store.SettingsRepositoryImpl
import com.yandex.practicum.middle_homework_5.settings.ui.SettingsViewModel
import com.yandex.practicum.middle_homework_5.settings.ui.contract.SettingsRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(androidApplication()) }
    viewModel { SettingsViewModel(get()) }
}