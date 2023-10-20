package com.daeng.okkal.data.room

import android.graphics.Color
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
            var id: String = Define.CLIENT_ID,
            @ColumnInfo(name = Define.COL_INIT_APP_SHIRTS_COLOR)
            var shirtsColor: Int? = Color.WHITE,
            @ColumnInfo(name = Define.COL_INIT_APP_PANTS_COLOR)
            var pantsColor: Int? = Color.BLACK
//            @ColumnInfo(name = Define.COL_INIT_APP_COLOR_LIST)
//            var colorList: List<Int>? = null
        )
    }
}