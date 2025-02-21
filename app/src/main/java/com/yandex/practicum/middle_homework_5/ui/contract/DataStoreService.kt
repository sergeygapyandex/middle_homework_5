package com.yandex.practicum.middle_homework_5.ui.contract

import com.yandex.practicum.middle_homework_5.data.data_store.SettingContainer
import kotlinx.coroutines.flow.StateFlow

interface DataStoreService {
    val settingData: StateFlow<SettingContainer>
    suspend fun saveSetting(periodic: Long, delayed: Long)
    suspend fun readSetting()
}