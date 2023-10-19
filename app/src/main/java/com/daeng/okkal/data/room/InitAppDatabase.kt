package com.daeng.okkal.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.daeng.okkal.data.room.RoomEntity.Companion.InitApp
import com.daeng.okkal.global.Define

/**
 * Created by JDY on 2023-10-16
 */
@Database(entities = [InitApp::class], version = 1, exportSchema = false)
abstract class InitAppDatabase: RoomDatabase(){
    abstract fun initAppDao(): RoomDao.InitAppDao
    companion object {
        private var instance: InitAppDatabase? = null

        @Synchronized
        fun inst(context: Context): InitAppDatabase? {
            if (instance == null) {
                kotlin.synchronized(InitAppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        InitAppDatabase::class.java,
                        Define.TABLE_INIT_APP
                    ).build()
                }
            }
            return instance
        }
    }
}