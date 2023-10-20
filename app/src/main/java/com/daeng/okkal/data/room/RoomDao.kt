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

        @Query("SELECT * FROM " + Define.TABLE_INIT_APP)
        fun getAll() : List<RoomEntity.Companion.InitApp>

        // 저장된 색상 리스트만 불러오기
        @Query("SELECT " + Define.COL_INIT_APP_SHIRTS_COLOR + " FROM " + Define.TABLE_INIT_APP)
        fun getShirtsColor(): Int

        @Query("SELECT " + Define.COL_INIT_APP_PANTS_COLOR + " FROM " + Define.TABLE_INIT_APP)
        fun getPantsColor(): Int

        @Query(
            "UPDATE " + Define.TABLE_INIT_APP + " SET " +
                    Define.COL_INIT_APP_SHIRTS_COLOR + " = :color"
        )
        fun updateShirtsColor(color: Int) // 상의 색상 업데이트 쿼리


        @Query(
            "UPDATE " + Define.TABLE_INIT_APP + " SET " +
                    Define.COL_INIT_APP_PANTS_COLOR + " = :color"
        )
        fun updatePantsColor(color: Int) // 하의 색상 업데이트 쿼리

    }
}