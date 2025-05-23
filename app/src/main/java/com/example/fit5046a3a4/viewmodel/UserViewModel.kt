package com.example.fit5046a3a4.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.data.UserEntity
import com.example.fit5046a3a4.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.FirebaseAuth



@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val repository = UserRepository(AppDatabase.get(application).userDao())

    private val _userState = MutableStateFlow<UserEntity?>(null)

    private val _cloudCredit = MutableStateFlow(0.0)
    val cloudCredit: StateFlow<Double> = _cloudCredit

    val userState: StateFlow<UserEntity?> = _userState.asStateFlow()

    init {
        loadLastUserFromDatabase()
    }

    fun setUser(user: UserEntity) {
        _userState.value = user
        saveLastUserEmail(user.email)
    }

    fun clearUser() {
        _userState.value = null
        clearLastUserEmail()
    }

    fun addUser(user: UserEntity) {
        viewModelScope.launch {
            repository.addUser(user)
            _userState.value = user
            saveLastUserEmail(user.email)
        }
    }

    suspend fun addUserReturnId(user: UserEntity): Long {
        return repository.addUserReturnId(user)
    }

    fun fetchUserCredits(email: String) {
        viewModelScope.launch {
            val credit = fetchUserDollars(email)
            _cloudCredit.value = credit
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            repository.updateUser(user)
            _userState.value = user
            saveLastUserEmail(user.email)
        }
    }

    fun updateUserInFirebase(user: UserEntity, onComplete: (() -> Unit)? = null) {
        val userDoc = FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.email)

        val updatedData = mapOf(
            "username" to user.username
        )

        userDoc.update(updatedData)
            .addOnSuccessListener {
                Log.d("UserViewModel", " Username updated in Firestore")
                onComplete?.invoke()
            }
            .addOnFailureListener { e ->
                Log.e("UserViewModel", " Failed to update username: ${e.message}")
            }
    }


    suspend fun getUserNow(email: String): UserEntity? {
        return repository.getUserByEmail(email)
    }
    //fetch dollars from firebase
    suspend fun fetchUserDollars(email: String): Double {
        val db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")
        val querySnapshot = usersRef.whereEqualTo("email", email).get().await()
        val doc = querySnapshot.documents.firstOrNull()
        return doc?.getDouble("dollars") ?: 0.0
    }

    fun printAllUsers() {
        viewModelScope.launch {
            val users = repository.allUsers.firstOrNull()
            users?.forEach {
                Log.d("UserDebug", "User: id=${it.id}, username=${it.username}, email=${it.email}")
            } ?: Log.d("UserDebug", "No users found")
        }
    }

    fun clearAllUsers() {
        viewModelScope.launch {
            repository.clearUsers()
            _userState.value = null
            clearLastUserEmail()
        }
    }

    private fun saveLastUserEmail(email: String) {
        val prefs = getApplication<Application>().getSharedPreferences("user_prefs", 0)
        prefs.edit().putString("last_user_email", email).apply()
    }

    private fun clearLastUserEmail() {
        val prefs = getApplication<Application>().getSharedPreferences("user_prefs", 0)
        prefs.edit().remove("last_user_email").apply()
    }

    private fun loadLastUserFromDatabase() {
        viewModelScope.launch {
            val prefs = getApplication<Application>().getSharedPreferences("user_prefs", 0)
            val lastEmail = prefs.getString("last_user_email", null)
            if (lastEmail != null) {
                _userState.value = repository.getUserByEmail(lastEmail)
            }
        }
    }
    fun addUserAndReturnId(user: UserEntity, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val id = repository.addUserReturnId(user)
            val completeUser = user.copy(id = id)
            _userState.value = completeUser
            saveLastUserEmail(completeUser.email)
            onResult(id)
        }
    }

    fun syncUserFromFirebase(email: String, onComplete: (() -> Unit)? = null) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance().collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val doc = querySnapshot.documents.firstOrNull()
                if (doc != null) {
                    val id = doc.getLong("id")?.toLong() ?: 0L
                    val username = doc.getString("username") ?: ""
                    val dollars = doc.getDouble("dollars") ?: 0.0
                    val points = (doc.getLong("points") ?: 0L).toInt()

                    val user = UserEntity(
                        id = id,
                        username = username,
                        email = email,
                        password = "",
                        dollars = dollars,
                        points = points
                    )
                    viewModelScope.launch {
                        repository.addUser(user)
                        setUser(user)
                        onComplete?.invoke()
                    }
                }
            }

    }

}