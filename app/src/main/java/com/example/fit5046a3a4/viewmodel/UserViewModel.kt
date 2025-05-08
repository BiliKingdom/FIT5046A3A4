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

@HiltViewModel
class UserViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val repository = UserRepository(AppDatabase.get(application).userDao())

    private val _userState = MutableStateFlow<UserEntity?>(null)
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

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            repository.updateUser(user)
            _userState.value = user
            saveLastUserEmail(user.email)
        }
    }

    suspend fun getUserNow(email: String): UserEntity? {
        return repository.getUserByEmail(email)
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

    /** 🔒 SharedPreferences 管理用户持久化登录 **/
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
}
