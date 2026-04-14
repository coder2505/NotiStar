package com.example.notistar.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [RoomEntity::class], version = 1)
abstract class RoomDatabase : RoomDatabase() {

    abstract fun RoomDao() : RoomDao
}