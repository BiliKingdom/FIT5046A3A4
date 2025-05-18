package com.example.fit5046a3a4.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface FoodDao {

    @Query("SELECT * FROM food_categories WHERE restaurantId = :restaurantId")
    fun getCategoriesByRestaurant(restaurantId: Long): Flow<List<FoodCategoryEntity>>

    @Query("SELECT * FROM food_items WHERE categoryId = :categoryId")
    fun getItemsByCategory(categoryId: Long): Flow<List<FoodItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: FoodCategoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: FoodItemEntity): Long

    @Query("DELETE FROM food_categories")
    suspend fun clearCategories()

    @Query("DELETE FROM food_items")
    suspend fun clearItems()

    @Query("SELECT * FROM food_items")
    suspend fun getAllOnce(): List<FoodItemEntity>

    @Query("SELECT * FROM food_categories")
    suspend fun getAllCategoriesOnce(): List<FoodCategoryEntity>

    @Query("SELECT * FROM food_items WHERE categoryId = :categoryId")
    suspend fun getFoodsByCategoryOnce(categoryId: Long): List<FoodItemEntity>

}
