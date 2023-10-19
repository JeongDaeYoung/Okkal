package com.daeng.okkal.util

import android.content.Context
import com.daeng.okkal.data.room.InitAppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by JDY on 2023-10-16
 */
class DataHelper {
    fun getInitApp(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val colorList = InitAppDatabase.inst(context)!!.initAppDao().getColorList()
        }
    }
}