package com.example.fit5046a3a4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val quantity: Int,
    val imageRes: Int,
    val price: Double          // 用 Double 方便汇总
)
