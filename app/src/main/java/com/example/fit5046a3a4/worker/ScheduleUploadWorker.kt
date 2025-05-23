package com.example.fit5046a3a4.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder


fun scheduleUploadWorker(context: Context) {
    val now = Calendar.getInstance()
    val midnight = now.clone() as Calendar
    midnight.set(Calendar.HOUR_OF_DAY, 0)
    midnight.set(Calendar.MINUTE, 0)
    midnight.set(Calendar.SECOND, 0)
    midnight.set(Calendar.MILLISECOND, 0)
    midnight.add(Calendar.DAY_OF_MONTH, 1)

    val initialDelay = midnight.timeInMillis - now.timeInMillis

    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    val request = PeriodicWorkRequestBuilder<UploadToFirebaseWorker>(15, TimeUnit.MINUTES)
        .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()


    val wm = WorkManager.getInstance(context)
    val immediate = OneTimeWorkRequestBuilder<UploadToFirebaseWorker>()
    .setConstraints(constraints)
    .build()
    wm.enqueueUniqueWork(
    "upload_to_firebase_now",
    ExistingWorkPolicy.REPLACE,
    immediate
    )



    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "upload_to_firebase",
        ExistingPeriodicWorkPolicy.UPDATE,
        request
    )
}
