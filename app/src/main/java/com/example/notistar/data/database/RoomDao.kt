package com.example.notistar.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomDao {

    @Query("select * from RoomEntity")
    fun getAll(): List<RoomEntity>

    @Insert
    fun insert(vararg data:RoomEntity)

}