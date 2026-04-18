package com.example.notistar.data.repository

import com.example.notistar.data.database.RoomDao
import com.example.notistar.data.database.RoomEntity
import javax.inject.Inject

class UpdateDBWithIncomingNotifications @Inject constructor(val roomDao: RoomDao) {

     fun insertNotificationInDB(notification: RoomEntity){

        roomDao.insert(notification)

    }

}