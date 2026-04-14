package com.example.notistar.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoomEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "package_name")val packageName : String,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "desc") val textDesc : String?,
    @ColumnInfo(name = "time") val time : Long,
)