package com.example.fit5046a3a4.data

class UserRepository(private val dao: UserDao) {
    val allUsers = dao.getAllUsers()

    suspend fun addUser(user: UserEntity) = dao.insertUser(user)
    suspend fun deleteUser(user: UserEntity) = dao.deleteUser(user)
    suspend fun clearUsers() = dao.clearUsers()
    suspend fun getUserByEmail(email: String) = dao.getUserByEmail(email)
    suspend fun updateUser(user: UserEntity) = dao.updateUser(user)
}

