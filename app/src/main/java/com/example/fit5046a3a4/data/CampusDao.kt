package com.example.fit5046a3a4.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CampusDao {

    @Query("SELECT * FROM campuses")
    fun getAllCampuses(): Flow<List<CampusEntity>>

    @Query("SELECT * FROM campuses WHERE name = :campusName LIMIT 1")
    suspend fun getCampusByName(campusName: String): CampusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampus(campus: CampusEntity): Long

    @Query("DELETE FROM campuses")
    suspend fun clearAll()

    @Query("SELECT * FROM campuses")
    suspend fun getAllOnce(): List<CampusEntity>


}
