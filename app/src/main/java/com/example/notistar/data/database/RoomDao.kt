package com.example.notistar.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Query("select * from RoomEntity")
    fun getAll(): Flow<List<RoomEntity>>

    @Insert
    fun insert(notif:RoomEntity)

}