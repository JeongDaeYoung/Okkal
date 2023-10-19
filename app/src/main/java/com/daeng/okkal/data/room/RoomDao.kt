package com.daeng.okkal.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.daeng.okkal.global.Define

/**
 * Created by JDY on 2023-10-16
 */
class RoomDao {
    @Dao
    interface InitAppDao {
        @Insert
        fun insert(initApp: RoomEntity.Companion.InitApp)

        @Update
        fun update(initApp: RoomEntity.Companion.InitApp)

        // 저장된 색상 리스트만 불러오기
        @Query("SELECT " + Define.COL_INIT_APP_COLOR_LIST + " FROM " + Define.TABLE_INIT_APP)
        fun getColorList(): List<Int>
    }
}