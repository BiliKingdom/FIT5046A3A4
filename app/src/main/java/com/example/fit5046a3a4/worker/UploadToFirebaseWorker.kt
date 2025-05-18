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

            // === 上传购物车条目 ===
            val cartItems = db.cartDao().getAllOnce() // 你需要实现 getAllOnce()
            cartItems.forEach { cartItem ->
                val docRef = firebaseDb.collection("cart_items").document(cartItem.id.toString())
                batch.set(docRef, cartItem)
                Log.d("UploadToFirebaseWorker", "Uploading cartItem: $cartItem")
            }

            // === 上传用户 ===
            val users = db.userDao().getAllOnce() // 你需要实现 getAllOnce()
            users.forEach { user ->
                val docRef = firebaseDb.collection("users").document(user.id.toString())
                batch.set(docRef, user)
                Log.d("UploadToFirebaseWorker", "Uploading user: $user")
            }

            // === 上传校区 ===
            val campuses = db.campusDao().getAllOnce() // 你需要实现 getAllOnce()
            campuses.forEach { campus ->
                val docRef = firebaseDb.collection("campuses").document(campus.id.toString())
                batch.set(docRef, campus)
                Log.d("UploadToFirebaseWorker", "Uploading campus: $campus")
            }

            // === 上传餐厅 ===
            val restaurants = db.restaurantDao().getAllOnce() // 你需要实现 getAllOnce()
            restaurants.forEach { restaurant ->
                val docRef = firebaseDb.collection("restaurants").document(restaurant.id.toString())
                batch.set(docRef, restaurant)
                Log.d("UploadToFirebaseWorker", "Uploading restaurant: $restaurant")
            }

            // === 上传食品/分类依次添加 ==
            val categories = db.foodDao().getAllCategoriesOnce()
            categories.forEach { category ->
                val catRef = firebaseDb.collection("food_categories").document(category.id.toString())
                batch.set(catRef, category)
                Log.d("UploadToFirebaseWorker", "Uploading category: $category")

                // 2. 每个类别下上传所有food_items
                val foods = db.foodDao().getFoodsByCategoryOnce(category.id)
                foods.forEach { food ->
                    val foodRef = catRef.collection("food_items").document(food.id.toString())
                    batch.set(foodRef, food)
                    Log.d("UploadToFirebaseWorker", "Uploading food: $food")
                }
            }

            batch.commit().await()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
