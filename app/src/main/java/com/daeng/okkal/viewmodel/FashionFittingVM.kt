package com.daeng.okkal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daeng.okkal.data.room.RoomEntity
import com.daeng.okkal.global.Define
import com.daeng.okkal.model.FashionFittingRepository
import com.daeng.okkal.util.RecentColorLinkedList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FashionFittingVM @Inject constructor(private val myRepository : FashionFittingRepository): ViewModel() {
    private val _selPart : MutableLiveData<Int> = MutableLiveData(Define.SEL_SHIRTS)
    private val _shirtsColor : MutableLiveData<Int> = MutableLiveData()
    private val _pantsColor : MutableLiveData<Int> = MutableLiveData()
    private val _colorList : MutableLiveData<RecentColorLinkedList<Int>> = MutableLiveData()

    val selPart : LiveData<Int>
        get() = _selPart
    val shirtsColor : LiveData<Int>
        get() = _shirtsColor
    val pantsColor : LiveData<Int>
        get() = _pantsColor
    val colorList : LiveData<RecentColorLinkedList<Int>>
        get() = _colorList

    init {
        viewModelScope.launch {
            myRepository.initColorData()
            loadColorData()
        }
    }

    private fun loadShirtsColor() {
        viewModelScope.launch{
            _shirtsColor.value = myRepository.getShirtsColor()
        }
    }

    private fun loadPantsColor() {
        viewModelScope.launch{
            _pantsColor.value = myRepository.getPantsColor()
        }
    }

    private fun loadColorData() {
        viewModelScope.launch {
            val data: RoomEntity.Companion.InitApp = myRepository.getColorData()
            _shirtsColor.value = data.shirtsColor
            _pantsColor.value = data.pantsColor
            _colorList.value = RecentColorLinkedList.fromList(data.colorData.colorList)
        }
    }

    fun updateSelPart(part : Int) {
        _selPart.value = part
    }

    private fun updateShirtsColor(color: Int) {
        myRepository.updateShirtsColor(color)
        _shirtsColor.postValue(color)
    }

    private fun updatePantsColor(color: Int) {
        myRepository.updatePantsColor(color)
        _pantsColor.postValue(color)
    }

    private fun addColorList(color : Int) {
        val list = _colorList.value
        list!!.addColor(color)
        myRepository.updateColorList(ArrayList(list))
        _colorList.postValue(list!!)
    }


    /*
    * 현재 선택된 부위에 색상 업데이트 및 최근 색상 리스트에 추가
    * */
    fun setNewPartColor(color: Int) {
        when(selPart.value) {
            Define.SEL_SHIRTS -> updateShirtsColor(color)
            Define.SEL_PANTS -> updatePantsColor(color)
        }
        addColorList(color)
    }


    /*
    * 현재 선택된 부위에 색상 업데이트
    * */
    fun setRecentPartColor(color: Int) {
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