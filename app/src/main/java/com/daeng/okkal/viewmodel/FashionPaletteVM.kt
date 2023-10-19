package com.daeng.okkal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.daeng.okkal.global.Define
import com.daeng.okkal.model.FashionPaletteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FashionPaletteVM @Inject constructor(): ViewModel() {
    val selPart : LiveData<Int> = FashionPaletteRepository.getSelPart()
    val shirtsColor : LiveData<Int> = FashionPaletteRepository.getShirtsColor()
    val pantsColor : LiveData<Int> = FashionPaletteRepository.getPantsColor()



    fun updateSelPart(part : Int) {
        FashionPaletteRepository.updateSelPart(part)
    }

    fun updateShirtsColor(color : Int) {
        FashionPaletteRepository.updateShirtsColor(color)
    }

    fun updatePantsColor(color : Int) {
        FashionPaletteRepository.updatePantsColor(color)
    }


    /*
    * 현재 선택되지 않은 파트의 인덱스를 리턴
    * */
    fun getOtherPartColor(): Int? {
        var color: Int? = null
        when(selPart.value) {
            Define.SEL_SHIRTS -> color = pantsColor.value
            Define.SEL_PANTS -> color = shirtsColor.value
        }
        return color
    }
}