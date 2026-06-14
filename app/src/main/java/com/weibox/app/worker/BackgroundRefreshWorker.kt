package com.weibox.app.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.weibox.app.data.prefs.AppPreferences
import com.weibox.app.data.repository.WeiboRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class BackgroundRefreshWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: WeiboRepository,
    private val prefs: AppPreferences
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        runCatching { repository.refreshTimelineBackground() }
            .onSuccess { prefs.saveLastRefreshTime(System.currentTimeMillis()) }
        return Result.success()
    }

    companion object {
        private const val WORK_NAME = "bg_refresh"

        fun schedule(context: Context) {
            val request = PeriodicWorkRequestBuilder<BackgroundRefreshWorker>(15, TimeUnit.MINUTES)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .build()
                )
                .build()
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, request)
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
