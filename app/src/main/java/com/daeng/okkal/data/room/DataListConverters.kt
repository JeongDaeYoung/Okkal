package com.daeng.okkal.data.room

import androidx.room.TypeConverter
import com.daeng.okkal.data.ColorData
import com.google.gson.Gson

/**
 * Created by JDY on 2023-10-22
 */
class DataListConverters {
    @TypeConverter
    fun listToJson(value: ColorData?) : String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String?) : ColorData? {
        return Gson().fromJson(value, ColorData::class.java)
    }
}