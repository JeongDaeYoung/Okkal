package com.daeng.okkal.viewmodel

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daeng.okkal.global.Define
import com.daeng.okkal.model.FashionPaletteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FashionPaletteVM @Inject constructor(private val myRepository : FashionPaletteRepository): ViewModel() {
    private val _selPart : MutableLiveData<Int> = MutableLiveData(Define.SEL_SHIRTS)
    private val _shirtsColor : MutableLiveData<Int> = MutableLiveData(Color.WHITE)
    private val _pantsColor : MutableLiveData<Int> = MutableLiveData(Color.BLACK)

    val selPart : LiveData<Int>
        get() = _selPart
    val shirtsColor : LiveData<Int>
        get() = _shirtsColor
    val pantsColor : LiveData<Int>
        get() = _pantsColor

    init {
        getShirtsColor()
        getPantsColor()
        myRepository.initColorData()
    }

    private fun getShirtsColor() {
        viewModelScope.launch{
            _shirtsColor.value = myRepository.getShirtsColor()
        }
    }

    private fun getPantsColor() {
        viewModelScope.launch{
            _pantsColor.value = myRepository.getPantsColor()
        }
    }

    fun updateSelPart(part : Int) {
        _selPart.value = part
    }

    fun updateShirtsColor(color: Int) {
        myRepository.updateShirtsColor(color)
        _shirtsColor.postValue(color)
    }

    fun updatePantsColor(color: Int) {
        myRepository.updatePantsColor(color)
        _pantsColor.postValue(color)
    }


    /*
    * 현재 선택된 부위에 색상 업데이트
    * */
    fun setSelPartColor(color: Int) {
        when(selPart.value) {
            Define.SEL_SHIRTS -> updateShirtsColor(color)
            Define.SEL_PANTS -> updatePantsColor(color)
        }
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