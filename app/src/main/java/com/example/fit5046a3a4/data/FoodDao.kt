package com.example.fit5046a3a4.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FoodDao {

    // 全局取所有类别（不按餐厅筛）
    @Query("SELECT * FROM food_categories")
    fun getAllCategories(): Flow<List<FoodCategoryEntity>>

    // 取指定类别下所有菜品
    @Query("SELECT * FROM food_items WHERE categoryId = :categoryId AND restaurantId = :restaurantId")
    fun getItemsByCategoryAndRestaurant(
        categoryId: Long,
        restaurantId: Long
    ): Flow<List<FoodItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: FoodCategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: FoodItemEntity): Long

    @Query("DELETE FROM food_categories")
    suspend fun clearCategories()

    @Query("DELETE FROM food_items")
    suspend fun clearItems()

    // 同步用——拿一次全量列表
    @Query("SELECT * FROM food_categories")
    suspend fun getAllCategoriesOnce(): List<FoodCategoryEntity>

    @Query("SELECT * FROM food_items")
    suspend fun getAllOnce(): List<FoodItemEntity>
}
