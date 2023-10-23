package com.daeng.okkal.model

import com.daeng.okkal.data.ColorData
import com.daeng.okkal.data.room.RoomDao
import com.daeng.okkal.data.room.RoomEntity
import com.daeng.okkal.global.Define
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by JDY on 2023-10-18
 */
class FashionFittingRepository @Inject constructor(private val myDao : RoomDao.InitAppDao) {
    /*
    * 상의 색상을 roomDB에서 select
    * */
    suspend fun getShirtsColor() : Int{
        return withContext(Dispatchers.IO) {
            myDao.getShirtsColor()
        }
    }

    /*
    * 하의 색상을 roomDB에서 select
    * */
    suspend fun getPantsColor() : Int {
        return withContext(Dispatchers.IO) {
            myDao.getPantsColor()
        }
    }

    /*
    * 색상 정보 전체를 roomDB에서 select
    * */
    suspend fun getColorData() : RoomEntity.Companion.InitApp {
        return withContext(Dispatchers.IO) {
            myDao.getAll(Define.CLIENT_ID)
        }
    }


    /*
    * 상의 색상을 roomDB에 업데이트
    * */
    fun updateShirtsColor(color: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            myDao.updateShirtsColor(color)
        }
    }


    /*
    * 하의 색상을 roomDB에 업데이트
    * */
    fun updatePantsColor(color: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            myDao.updatePantsColor(color)
        }
    }

    /*
    * 색상 리스트를 roomDB에 업데이트
    * */
    fun updateColorList(colorList: List<Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            myDao.updateColorList(ColorData(colorList))
        }
    }


    /*
    * 색상 데이터가 roomDB에 없는 경우 기본 데이터를 생성하여 삽입
    * */
    suspend fun initColorData() {
        return withContext(Dispatchers.IO) {
            if (myDao.getAll(Define.CLIENT_ID) == null) {
                myDao.insert(RoomEntity.Companion.InitApp())
            }
        }
    }
}