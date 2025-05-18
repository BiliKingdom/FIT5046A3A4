package com.example.fit5046a3a4.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM restaurants WHERE campusId = :campusId")
    fun getRestaurantsByCampus(campusId: Long): Flow<List<RestaurantEntity>>

    @Query("SELECT * FROM restaurants WHERE id = :restaurantId")
    suspend fun getRestaurantById(restaurantId: Long): RestaurantEntity?

    @Query("SELECT * FROM restaurants WHERE id = :restaurantId")
    fun getRestaurantByIdFlow(restaurantId: Long): Flow<RestaurantEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurant(restaurant: RestaurantEntity): Long

    @Query("DELETE FROM restaurants")
    suspend fun clearAll()
}
