package com.example.fit5046a3a4.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "campuses")
data class CampusEntity(
    @PrimaryKey val name: String,
    val latitude: Double,
    val longitude: Double
)



