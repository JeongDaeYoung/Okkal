package com.daeng.okkal.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.daeng.okkal.global.Define

/**
 * Created by JDY on 2023-10-16
 */
class RoomEntity {
    companion object {
        @Entity(tableName = Define.TABLE_INIT_APP)
        data class InitApp(
            @PrimaryKey
            val id: String = Define.CLIENT_ID,
            @ColumnInfo(Define.COL_INIT_APP_SHIRTS_COLOR)
            val shirtsColor: Int? = null,
            @ColumnInfo(Define.COL_INIT_APP_PANTS_COLOR)
            val pantsColor: Int? = null,
            @ColumnInfo(Define.COL_INIT_APP_COLOR_LIST)
            val colorList: List<Int>? = null
        )
    }
}