package com.example.fit5046a3a4.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index


@Entity(
    tableName = "restaurants",
    foreignKeys = [
        ForeignKey(
            entity = CampusEntity::class,
            parentColumns = ["id"],
            childColumns = ["campusId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["campusId"])]   // foreign key indices
)
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val campusId: Long
)
