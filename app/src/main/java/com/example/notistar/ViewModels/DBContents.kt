package com.example.notistar.ViewModels

import androidx.lifecycle.ViewModel
import com.example.notistar.data.database.RoomDao
import com.example.notistar.data.database.RoomEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DBContents @Inject constructor(val roomDao: RoomDao) : ViewModel() {

    fun provideNotification() : Flow<List<RoomEntity>> {
        return roomDao.getAll()
    }


}