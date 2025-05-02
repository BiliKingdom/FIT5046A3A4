package com.example.fit5046a3a4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.fit5046a3a4.data.AppDatabase
import com.example.fit5046a3a4.data.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = AppDatabase.get(application).userDao()

    private val _userState = MutableStateFlow<UserEntity?>(null)
    val userState: StateFlow<UserEntity?> = _userState.asStateFlow()

    fun loadUser(email: String) {
        viewModelScope.launch {
            _userState.value = userDao.getUserByEmail(email)
        }
    }

    fun addUser(user: UserEntity) {
        viewModelScope.launch {
            userDao.insertUser(user)
            _userState.value = user
        }
    }

    fun updateUser(updatedUser: UserEntity) {
        viewModelScope.launch {
            userDao.updateUser(updatedUser)
            _userState.value = updatedUser
        }
    }

    fun clearUser() {
        _userState.value = null
    }

    suspend fun getUserNow(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }

    fun setUser(user: UserEntity) {
        _userState.value = user
    }

}
