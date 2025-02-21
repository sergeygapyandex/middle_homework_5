package com.yandex.practicum.middle_homework_5.data.work_manager

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.yandex.practicum.middle_homework_5.data.data_store.SettingContainer.Companion.DEFAULT_REFRESH_PERIOD
import com.yandex.practicum.middle_homework_5.data.data_store.SettingContainer.Companion.FIST_LAUNCH_DELAY
import com.yandex.practicum.middle_homework_5.ui.contract.DataStoreService
import com.yandex.practicum.middle_homework_5.ui.contract.WorkManagerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class WorkManagerServiceImp(
    private val context: Context,
    private val dataStoreService: DataStoreService,
    private val scope: CoroutineScope = CoroutineScope(Job() + Dispatchers.IO)
) : WorkManagerService {
    private var period:Long = DEFAULT_REFRESH_PERIOD
    private var delayed: Long = FIST_LAUNCH_DELAY

    init {
        scope.launch {
            dataStoreService.settingData.collect{ setting ->
                period = setting.periodic
                delayed = setting.delayed
                Log.i(TAG, "DataStoreService get data : period = $period | delayed $delayed")
                launchRefreshWork()
            }
        }
    }

    private fun createConstraints(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(networkType = NetworkType.CONNECTED)
            .build()
    }

    private fun createRequest(repeat: Long, delayed: Long): PeriodicWorkRequest {
        val networkConstraints = createConstraints()
        return PeriodicWorkRequestBuilder<RefreshWorker>(
            repeatInterval = repeat,
            TimeUnit.MINUTES
        ).setInitialDelay(duration = delayed, TimeUnit.SECONDS)
            .setConstraints(constraints = networkConstraints)
            .build()
    }

    override fun launchRefreshWork() {
        val request: PeriodicWorkRequest =
            createRequest(repeat = period, delayed = delayed)
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName = REFRESH_WORK_NAME,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request = request
        )
    }

    override fun cancelRefreshWork() {
        WorkManager.getInstance(context).cancelUniqueWork(uniqueWorkName = REFRESH_WORK_NAME)
    }


    companion object {
        const val REFRESH_WORK_NAME = "Refresh work"
    }
}