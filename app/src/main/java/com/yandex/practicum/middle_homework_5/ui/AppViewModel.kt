package com.yandex.practicum.middle_homework_5.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yandex.practicum.middle_homework_5.data.NewsRemoteMediator
import com.yandex.practicum.middle_homework_5.data.data_store.SettingContainer
import com.yandex.practicum.middle_homework_5.data.database.NewsDatabase
import com.yandex.practicum.middle_homework_5.data.database.entity.News
import com.yandex.practicum.middle_homework_5.ui.contract.DataStoreService
import com.yandex.practicum.middle_homework_5.ui.contract.NewsService
import com.yandex.practicum.middle_homework_5.ui.contract.WorkManagerService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AppViewModel(
    private val newsService: NewsService,
    private val newsDatabase: NewsDatabase,
    private val workManagerService: WorkManagerService,
    private val dataStoreService: DataStoreService
) : ViewModel() {
    @OptIn(ExperimentalPagingApi::class)
    fun getNews(): Flow<PagingData<News>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE,
            ),
            pagingSourceFactory = {
                newsDatabase.getNewsDao().getNews()
            },
            remoteMediator = NewsRemoteMediator(
                newsService,
                newsDatabase,
            )
        ).flow

    fun launchPeriodicRefresh() {
        workManagerService.launchRefreshWork()
    }

    fun cancelPeriodicRefresh() {
        workManagerService.cancelRefreshWork()
    }

    fun saveSetting(periodic: Long, delayed: Long) {
        viewModelScope.launch {
            dataStoreService.saveSetting(periodic = periodic, delayed = delayed)
        }
    }

    fun getCurrentSetting(): SettingContainer {
        return dataStoreService.settingData.value
    }

    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 20
    }
}