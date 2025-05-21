package com.example.fit5046a3a4.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    // 查询购物车所有条目（Flow自动支持数据变化监听）
    @Query("SELECT * FROM cart_items")
    fun getAll(): Flow<List<CartItemEntity>>

    // 插入条目：返回新插入行的ID（Long）
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity): Long

    // 删除单个条目：返回删除的行数（Int）
    @Delete
    suspend fun delete(item: CartItemEntity): Int

    // 清空购物车：返回删除的总行数（Int）
    @Query("DELETE FROM cart_items")
    suspend fun clearAll(): Int

    //增加同步方法（一次性获取数据，而不是 Flow）
    @Query("SELECT * FROM cart_items")
    suspend fun getAllOnce(): List<CartItemEntity>

    @Query("SELECT * FROM cart_items WHERE name = :name AND imageRes = :imageRes LIMIT 1")
    suspend fun findByNameAndImage(name: String, imageRes: Int): CartItemEntity?

    @Update
    suspend fun update(item: CartItemEntity)


}