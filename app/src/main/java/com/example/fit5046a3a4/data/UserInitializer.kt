package com.example.fit5046a3a4.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.Timestamp

object UserInitializer {

    fun initializeFirestoreUserIfNew(email: String, username: String) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userDoc = Firebase.firestore.collection("users").document(uid)

        userDoc.get().addOnSuccessListener { snapshot ->
            if (!snapshot.exists()) {
                val newUser = mapOf(
                    "email" to email,
                    "username" to username,
                    "dollars" to 1.0,
                    "points" to 0
                )
                userDoc.set(newUser).addOnSuccessListener {
                    Log.d("UserInitializer", "User created in Firestore for $email")
                }.addOnFailureListener { e ->
                    Log.e("UserInitializer", "Failed to create user: ${e.message}")
                }
            } else {
                Log.d("UserInitializer", "User already exists in Firestore")
            }
        }.addOnFailureListener { e ->
            Log.e("UserInitializer", "Failed to load user doc: ${e.message}")
        }
    }

    // ✅ 读取 Firestore 中的 credit (dollars)，重命名为 fetchUserCredits
    fun fetchUserCredits(
        onSuccess: (Double) -> Unit,
        onFailure: (Exception) -> Unit = {}
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userDoc = Firebase.firestore.collection("users").document(uid)

        userDoc.get()
            .addOnSuccessListener { snapshot ->
                val credits = snapshot.getDouble("dollars") ?: 0.0
                Log.d("UserInitializer", "Fetched Firestore credits: $credits")
                onSuccess(credits)
            }
            .addOnFailureListener { e ->
                Log.e("UserInitializer", "Failed to fetch Firestore credits: ${e.message}")
                onFailure(e)
            }
    }

    fun uploadOrderToFirebase(
        orderId: String,
        userId: String,
        email: String,
        items: List<CartItemEntity>,
        total: Double,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()
        val orderData = hashMapOf(
            "orderId" to orderId,
            "userId" to userId,
            "email" to email,
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
    fun updateUserCredits(
        email: String,
        newCredit: Double,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(uid)
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
}
