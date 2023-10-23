package com.daeng.okkal.data.room

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.daeng.okkal.data.ColorData
import com.daeng.okkal.global.Define

/**
 * Created by JDY on 2023-10-16
 */
class RoomEntity {
    companion object {
        @Entity(tableName = Define.TABLE_INIT_APP)
        data class InitApp(
            @PrimaryKey
            var id: String = Define.CLIENT_ID,                                                // 고객 아이디 (주키)
            @ColumnInfo(name = Define.COL_INIT_APP_SHIRTS_COLOR)
            var shirtsColor: Int = Color.WHITE,                                               // 상의 색상
            @ColumnInfo(name = Define.COL_INIT_APP_PANTS_COLOR)
            var pantsColor: Int = Color.BLACK,                                                // 하의 색상
            @ColumnInfo(name = Define.COL_INIT_APP_COLOR_LIST)
            var colorData: ColorData = ColorData(listOf(Color.WHITE, Color.BLACK))            // 색상 리스트 (List 형식을 roomDB에 바로 넣을 경우 update 불가하여 객체로 생성)
        )
    }
}