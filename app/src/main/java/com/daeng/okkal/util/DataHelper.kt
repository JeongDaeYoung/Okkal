package com.daeng.okkal.util

import com.daeng.okkal.data.room.RoomDao
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by JDY on 2023-10-16
 */
class DataHelper @Inject constructor(private val initAppDao : RoomDao.InitAppDao) {
    suspend fun initColorData() {

    }
}