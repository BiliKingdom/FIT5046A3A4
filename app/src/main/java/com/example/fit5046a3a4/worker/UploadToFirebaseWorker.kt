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

            // === 0. 清理所有 restaurants 及其 food_items ===
            val restaurantsSnap = firebaseDb
                .collection("restaurants")
                .get()
                .await()
            for (restDoc in restaurantsSnap.documents) {
                // 0.1 删除这个 restaurant 底下的 food_items 子集合所有文档
                val itemsSnap = firebaseDb
                    .collection("restaurants")
                    .document(restDoc.id)
                    .collection("food_items")
                    .get()
                    .await()
                for (itemDoc in itemsSnap.documents) {
                    batch.delete(itemDoc.reference)
                }
                // 0.2 删除 restaurant 文档本身
                batch.delete(restDoc.reference)
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
                val ref = firebaseDb
                    .collection("users")
                    .document(user.id.toString())
                batch.set(ref, user)
                Log.d("UploadWorker", "Uploading user: $user")
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

            // 一次性提交所有删除+写入
            batch.commit().await()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
