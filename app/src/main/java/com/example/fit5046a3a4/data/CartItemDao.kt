package com.example.fit5046a3a4.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items")
    fun getAll(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity): Long

    @Delete
    suspend fun delete(item: CartItemEntity): Int

    @Query("DELETE FROM cart_items")
    suspend fun clearAll()

    @Query("SELECT * FROM cart_items")
    suspend fun getAllOnce(): List<CartItemEntity>

    @Query("SELECT * FROM cart_items WHERE name = :name AND imageRes = :imageRes LIMIT 1")
    suspend fun findByNameAndImage(name: String, imageRes: Int): CartItemEntity?

    @Update
    suspend fun update(item: CartItemEntity)
}