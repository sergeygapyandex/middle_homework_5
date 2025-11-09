package com.yandex.practicum.middle_homework_5.settings.data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.yandex.practicum.middle_homework_5.settings.ui.contract.SettingsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SettingsRepository {
    private val STORAGE_NAME = "Application setting"
    private val REFRESH_PERIOD_KEY = longPreferencesKey("REFRESH_PERIOD")
    private val FIRST_LAUNCH_DELAY_KEY = longPreferencesKey("FIRST_LAUNCH_DELAY")
    private val _settingData = MutableStateFlow(SettingContainer.initial)
    override val settingData = _settingData.asStateFlow()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORAGE_NAME)

    init {
        CoroutineScope(Job() + dispatcher).launch {
            readSetting()
        }
    }

    override suspend fun saveSetting(periodic: Long, delayed: Long) {
        withContext(dispatcher) {
            context.dataStore.edit { pref: MutablePreferences ->
                pref[REFRESH_PERIOD_KEY] = periodic
                pref[FIRST_LAUNCH_DELAY_KEY] = delayed
                _settingData.value = SettingContainer(periodic = periodic, delayed = delayed)
            }
        }
    }

    override suspend fun readSetting() {
        withContext(dispatcher) {
            context.dataStore.data.collect { pref: Preferences ->
                val periodic = pref[REFRESH_PERIOD_KEY] ?: SettingContainer.DEFAULT_REFRESH_PERIOD
                val delayed = pref[FIRST_LAUNCH_DELAY_KEY] ?: SettingContainer.FIST_LAUNCH_DELAY
                _settingData.value = SettingContainer(periodic = periodic, delayed = delayed)
            }
        }
    }
}