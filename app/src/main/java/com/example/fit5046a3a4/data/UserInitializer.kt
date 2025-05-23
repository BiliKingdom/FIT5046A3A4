package com.example.fit5046a3a4.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

object UserInitializer {
    fun initializeFirestoreUserIfNew(user: UserEntity) {
        val email = user.email
        val userDoc = FirebaseFirestore.getInstance().collection("users").document(email)

        userDoc.get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                val newUser = mapOf(
                    "id" to user.id,
                    "email" to user.email,
                    "username" to user.username,
                    "dollars" to user.dollars,
                    "points" to user.points
                )
                userDoc.set(newUser).addOnSuccessListener {
                    Log.d("UserInitializer", "✅ Created Firestore user with id=${user.id}")
                }.addOnFailureListener { e ->
                    Log.e("UserInitializer", "❌ Failed to create user: ${e.message}")
                }
            } else {
                Log.d("UserInitializer", "📄 User already exists in Firestore")
            }
        }.addOnFailureListener { e ->
            Log.e("UserInitializer", "🔥 Failed to load user doc: ${e.message}")
        }
    }

    fun fetchUserCredits(
        email: String,
        onSuccess: (Double) -> Unit,
        onFailure: (Exception) -> Unit = {}
    ) {
        FirebaseFirestore.getInstance().collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { query ->
                val doc = query.documents.firstOrNull()
                val credits = doc?.getDouble("dollars") ?: 0.0
                Log.d("UserInitializer", "✅ Fetched Firestore credits: $credits")
                onSuccess(credits)
            }
            .addOnFailureListener { e ->
                Log.e("UserInitializer", "❌ Failed to fetch Firestore credits: ${e.message}")
                onFailure(e)
            }
    }

    fun updateUserCredits(
        email: String,
        newCredit: Double,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        FirebaseFirestore.getInstance().collection("users")
            .document(email)
            .update("dollars", newCredit)
            .addOnSuccessListener {
                Log.d("UserInitializer", "✅ Updated user credits to $newCredit")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("UserInitializer", "❌ Failed to update user credits: ${e.message}")
                onFailure(e)
            }
    }

    fun uploadOrderToFirebase(
        orderId: String,
        user: UserEntity,
        items: List<CartItemEntity>,
        total: Double,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val orderData = hashMapOf(
            "orderId" to orderId,
            "userId" to user.id.toString(),
            "email" to user.email,
            "total" to total,
            "timestamp" to Timestamp.now(),
            "items" to items.map {
                mapOf(
                    "name" to it.name,
                    "quantity" to it.quantity,
                    "price" to it.price
                )
            }
        )

        db.collection("orders")
            .document(orderId)
            .set(orderData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}
