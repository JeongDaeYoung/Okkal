package com.daeng.okkal.model

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.daeng.okkal.global.Define

/**
 * Created by JDY on 2023-10-18
 */
object FashionPaletteRepository {
    private val selPart = MutableLiveData<Int>(Define.SEL_SHIRTS)
    private val shirtsColor = MutableLiveData<Int>(Color.WHITE)
    private val pantsColor = MutableLiveData<Int>(Color.BLACK)

    fun getSelPart() : LiveData<Int> {
        return selPart
    }
    fun getShirtsColor() : LiveData<Int> {
        return shirtsColor
    }
    fun getPantsColor() : LiveData<Int> {
        return pantsColor
    }


    fun updateSelPart(part : Int) {
        selPart.value = part
    }
    fun updateShirtsColor(color : Int) {
        shirtsColor.value = color
    }
    fun updatePantsColor(color : Int) {
        pantsColor.value = color
    }
}