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

            // === 0-4. Clear the remote campuses data ===
            val campusSnap = firebaseDb.collection("campuses").get().await()
            for (doc in campusSnap.documents) {
                batch.delete(doc.reference)
            }



            // === 1. 清理所有 food_categories 文档 ===
            val categoriesSnap = firebaseDb
                .collection("food_categories")
                .get()
                .await()
            for (catDoc in categoriesSnap.documents) {
                batch.delete(catDoc.reference)
            }

            // === 2. 上传购物车条目 ===
            val cartItems = db.cartDao().getAllOnce()
            cartItems.forEach { cartItem ->
                val ref = firebaseDb
                    .collection("cart_items")
                    .document(cartItem.id.toString())
                batch.set(ref, cartItem)
                Log.d("UploadWorker", "Uploading cartItem: $cartItem")
            }

            // === 3. 上传用户 ===
            val users = db.userDao().getAllOnce()
            users.forEach { user ->
                val userMap = mapOf(
                    "id" to user.id,  // 可以保留
                    "email" to user.email,
                    "username" to user.username,
                    "dollars" to user.dollars,
                    "points" to user.points
                )
                val ref = firebaseDb
                    .collection("users")
                    .document(user.email) // ✅ 用 email 作为主键
                batch.set(ref, userMap)
                Log.d("UploadWorker", "Uploading user: $userMap")
            }

            // === 4. 上传校区 ===
            val campuses = db.campusDao().getAllOnce()
            campuses.forEach { campus ->
                val ref = firebaseDb
                    .collection("campuses")
                    .document(campus.id.toString())
                batch.set(ref, campus)
                Log.d("UploadWorker", "Uploading campus: $campus")
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
