package com.yandex.practicum.middle_homework_5.data.work_manager

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yandex.practicum.middle_homework_5.data.SourceProvider

class RefreshWorker(
    context: Context,
    workParams: WorkerParameters
) : CoroutineWorker(context, workParams) {

    override suspend fun doWork(): Result {
        SourceProvider.pagingItems?.refresh()
        return Result.success()
    }
}