package com.daeng.okkal.view.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.graphics.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daeng.okkal.databinding.ColorRecommendedDialogBinding
import com.daeng.okkal.util.RecyclerViewDecoration
import com.daeng.okkal.view.adapter.RecommendedColorListAdapter
import com.orhanobut.logger.Logger

/**
 * Created by JDY on 2023-10-12
 */
class ColorRecommendedDialog(private val color: Int, private val viewCloseListener: ViewCloseListener): BaseDialogFragment<ColorRecommendedDialogBinding>(ColorRecommendedDialogBinding::inflate) {
    private var colorListAdapter: RecommendedColorListAdapter? = null
    private var colorList: HashSet<Int> = HashSet()
    private var selColor: Int? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i("onViewCreated()")

        binding.btnOk.setOnClickListener {
            if (selColor != null) viewCloseListener.onCloseEvent(selColor!!)
            dismiss()
        }

        setRecommendedColor()
    }


    /*
    * 추천 색상 셋
    * */
    private fun setRecommendedColor() {
        val hsvValue = FloatArray(3)                                  // 0 : 색상, 1 : 채도, 2 : 명도
        Color.RGBToHSV(color.red, color.green, color.blue, hsvValue)

        recursiveHsv(hsvValue.copyOf(), 0.2f)                        // 명도를 0.2씩 올려 색상 찾기
        recursiveHsv(hsvValue.copyOf(), -0.2f)                       // 명도를 0.2씩 내려 색상 찾기
        initColorList()
    }


    /*
    * 추천 색상을 구하는 재귀함수
    * */
    private fun recursiveHsv(hsvValue: FloatArray, offSet: Float) {
        hsvValue[2] += offSet

        if (hsvValue[2] > 0 && hsvValue[2] < 1) {
            // hsvValue[2]의 값(명도)이 0보다 크고 1보다 작을때 색상을 추가하고 재귀
            colorList.add(Color.HSVToColor(hsvValue))
            recursiveHsv(hsvValue, offSet)
        } else {
            // hsvValue[2]의 값(명도)이 0 이하 또는 1 이상인 경우 재귀를 종료
            return
        }
    }


    /*
    * 색상 리스트 리사이클러뷰 초기화
    * */
    private fun initColorList() {
        binding.listColor.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
            addItemDecoration(RecyclerViewDecoration(40,0))
            colorListAdapter = RecommendedColorListAdapter(::selListColor)
            colorListAdapter!!.setItem(colorList)
            adapter = colorListAdapter
        }
    }

    private fun selListColor(color: Int) {
        selColor = color
    }
}