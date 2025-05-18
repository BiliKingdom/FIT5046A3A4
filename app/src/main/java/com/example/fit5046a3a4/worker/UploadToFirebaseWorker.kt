import android.content.Context
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
            // 1. 读取本地数据库数据
            val db = AppDatabase.get(applicationContext)
            val cartItems = db.cartDao().getAllOnce() // 你需要实现这个 suspend 方法！

            // 2. 上传到 Firebase Firestore（可换成Realtime DB）
            val firebaseDb = FirebaseFirestore.getInstance()
            val batch = firebaseDb.batch()

            cartItems.forEach { cartItem ->
                val docRef = firebaseDb.collection("cart_items").document(cartItem.id.toString())
                batch.set(docRef, cartItem)
            }

            batch.commit().await() // 等待所有数据提交完毕
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry() // 失败自动重试
        }
    }
}
