package com.example.notistar.modules

import android.content.Context
import androidx.room.Room
import com.example.notistar.data.database.AppDataBase
import com.example.notistar.data.database.RoomDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DiModule {

    @Provides
    @Singleton
    fun providesDB(@ApplicationContext context: Context) : AppDataBase{

        return Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "NOTISTAR_DB"
        ).build()

    }

    @Provides
    @Singleton
    fun providesRoomDao(appDataBase: AppDataBase) : RoomDao{

        return appDataBase.RoomDao()

    }


}