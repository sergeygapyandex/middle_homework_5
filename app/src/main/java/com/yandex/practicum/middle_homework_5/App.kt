package com.yandex.practicum.middle_homework_5

import android.app.Application
import com.yandex.practicum.middle_homework_5.data.SourceProvider
import com.yandex.practicum.middle_homework_5.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SourceProvider.init(application = this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}