package com.example.fit5046a3a4.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

fun scheduleUploadWorker(context: Context) {
    val now = Calendar.getInstance()
    val midnight = now.clone() as Calendar
    midnight.set(Calendar.HOUR_OF_DAY, 0)
    midnight.set(Calendar.MINUTE, 0)
    midnight.set(Calendar.SECOND, 0)
    midnight.set(Calendar.MILLISECOND, 0)
    midnight.add(Calendar.DAY_OF_MONTH, 1) // 明天的午夜

    val initialDelay = midnight.timeInMillis - now.timeInMillis

    val request = PeriodicWorkRequestBuilder<UploadToFirebaseWorker>(15, TimeUnit.MINUTES)
        //.setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)  normal
        .setInitialDelay(0, TimeUnit.MILLISECONDS) // 立即测试
        .build()


    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "upload_to_firebase",
        ExistingPeriodicWorkPolicy.UPDATE,
        request
    )
}
