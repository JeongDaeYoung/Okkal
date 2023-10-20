package com.daeng.okkal.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.daeng.okkal.data.room.RoomEntity.Companion.InitApp
import com.daeng.okkal.global.Define
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by JDY on 2023-10-16
 */
@Database(entities = [InitApp::class], version = 1, exportSchema = false)
abstract class InitAppDatabase: RoomDatabase(){
    abstract fun initAppDao(): RoomDao.InitAppDao
//    companion object {
//        private var instance: InitAppDatabase? = null
//
//        @Synchronized
//        fun inst(context: Context): InitAppDatabase? {
//            if (instance == null) {
//                kotlin.synchronized(InitAppDatabase::class) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        InitAppDatabase::class.java,
//                        Define.TABLE_INIT_APP
//                    ).build()
//                }
//            }
//            return instance
//        }
//    }

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {
        @Provides
        @Singleton
        fun provideInitAppDatabase(@ApplicationContext context: Context): InitAppDatabase {
            return Room.databaseBuilder(
                context,
                InitAppDatabase::class.java,
                Define.TABLE_INIT_APP
            ).build()
        }

        @Provides
        fun provideInitAppDao(database: InitAppDatabase): RoomDao.InitAppDao {
            return database.initAppDao()
        }
    }
}