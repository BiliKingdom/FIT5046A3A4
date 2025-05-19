package com.example.fit5046a3a4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageRes: Int,
    val categoryId: Long,
    val restaurantId: Long
)


