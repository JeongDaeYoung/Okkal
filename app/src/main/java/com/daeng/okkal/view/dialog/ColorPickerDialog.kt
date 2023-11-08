package com.daeng.okkal.view.dialog

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.daeng.okkal.R
import com.daeng.okkal.databinding.ColorPickerDialogBinding
import com.orhanobut.logger.Logger
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

/**
 * Created by JDY on 2023-11-06
 */
class ColorPickerDialog(private val viewCloseListener: ViewCloseListener): BaseDialogFragment<ColorPickerDialogBinding>(ColorPickerDialogBinding::inflate) {
    private var selColor: Int? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.i("onViewCreated()")

        initColorPicker()                 // 색상 선택표 초기화

        binding.btnOk.setOnClickListener {
            if (selColor != null) viewCloseListener.onCloseEvent(selColor!!)
            dismiss()
        }
    }


    /*
    * 색상 선택표 초기화 작업
    * */
    private fun initColorPicker() {
        binding.colorPicker.attachBrightnessSlider(binding.brightnessBar)     //밝기 조절 슬라이더 연결

        binding.colorPicker.setColorListener(ColorEnvelopeListener { envelope, fromUser ->                             //색상 선택표에서 색상이 선택됐을때 할 작업
            val drawable = ContextCompat.getDrawable(activity, R.drawable.shape_circle_stroke) as GradientDrawable
            drawable.setColor(envelope.color)
            binding.selColor.setImageDrawable(drawable)                  // 현재 선택한 색상 표시
            binding.edtColorHex.setText("#" + envelope.hexCode)          // 현재 선택한 색상의 16진수값 표시
            selColor(envelope.color)                                     // 현재 선택한 색상을 리턴하기 위해 담아둠
        })
    }


    private fun selColor(color: Int) {
        selColor = color
    }
}