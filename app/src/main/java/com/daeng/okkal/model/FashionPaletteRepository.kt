package com.daeng.okkal.model

import com.daeng.okkal.data.room.RoomDao
import com.daeng.okkal.data.room.RoomEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by JDY on 2023-10-18
 */
class FashionPaletteRepository @Inject constructor(private val myDao : RoomDao.InitAppDao) {
    suspend fun getShirtsColor() : Int{
        return withContext(Dispatchers.IO) {
            myDao.getShirtsColor()
        }
    }

    suspend fun getPantsColor() : Int {
        return withContext(Dispatchers.IO) {
            myDao.getPantsColor()
        }
    }

    fun updateShirtsColor(color: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            myDao.updateShirtsColor(color)
        }
    }

    fun updatePantsColor(color: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            myDao.updatePantsColor(color)
        }
    }

    fun initColorData() {
        CoroutineScope(Dispatchers.IO).launch {
            if (myDao.getAll().isEmpty()) {
                myDao.insert(RoomEntity.Companion.InitApp())
            }
        }
    }
}