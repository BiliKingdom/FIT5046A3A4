package com.example.fit5046a3a4.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.fit5046a3a4.data.AppDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await

class UploadToFirebaseWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val db = AppDatabase.get(applicationContext)
            val firebaseDb = FirebaseFirestore.getInstance()
            val batch = firebaseDb.batch()

            //Clear the remote campuses data
            val campusSnap = firebaseDb.collection("campuses").get().await()
            for (doc in campusSnap.documents) {
                batch.delete(doc.reference)
            }

            // Clear the remote cart_item data
            val remoteCartSnap = firebaseDb.collection("cart_items").get().await()
            remoteCartSnap.documents.forEach { batch.delete(it.reference) }

            // upload campuses
            val campuses = db.campusDao().getAllOnce()
            campuses.forEach { campus ->
                val ref = firebaseDb
                    .collection("campuses")
                    .document(campus.name)
                batch.set(ref, campus)
                Log.d("UploadWorker", "Uploading campus: $campus")
            }

            // upload cart_item
            val cartItems = db.cartDao().getAllOnce()
            cartItems.forEach { cartItem ->
                val ref = firebaseDb
                    .collection("cart_items")
                    .document(cartItem.id.toString())
                batch.set(ref, cartItem)
                Log.d("UploadWorker", "Uploading cartItem: $cartItem")
            }

            // upload user
            val users = db.userDao().getAllOnce()
            users.forEach { user ->
                val userMap = mapOf(
                    "id" to user.id,
                    "email" to user.email,
                    "username" to user.username,
                    "dollars" to user.dollars,
                    "points" to user.points
                )
                val ref = firebaseDb
                    .collection("users")
                    .document(user.email)
                batch.set(ref, userMap)
                Log.d("UploadWorker", "Uploading user: $userMap")
            }

            // Submit all deletions and writes at once
            batch.commit().await()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
