package com.daeng.okkal.view.dialog

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.*
import com.daeng.okkal.R
import com.daeng.okkal.databinding.PictureDialogBinding
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class PhotoDialog(private val photo: Bitmap) : BaseDialogFragment<PictureDialogBinding>(PictureDialogBinding::inflate){
    private var viewCloseListener: ViewCloseListener? = null
    private var selColor : Int? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initColorPicker()                              // colorPickerView 초기화 작업

        binding.btnOk.setOnClickListener {
            viewCloseListener!!.onCloseEvent(selColor)
            dismiss()
        }
    }

    /*
    * colorPickerView 초기화 작업
    * */
    private fun initColorPicker() {
        binding.colorPicker.setPaletteDrawable(BitmapDrawable(activity.resources, photo))        // 비트맵 Drawable로 변환후
        binding.colorPicker.attachBrightnessSlider(binding.brightnessBar)     //밝기 조절 슬라이더 연결

        binding.colorPicker.setColorListener(ColorEnvelopeListener { envelope, fromUser ->                             //색상 선택표에서 색상이 선택됐을때 할 작업
            val drawable = ContextCompat.getDrawable(activity, R.drawable.shape_circle_stroke) as GradientDrawable
            drawable.setColor(envelope.color)
            binding.selColor.setImageDrawable(drawable)                  // 현재 선택한 색상 표시
            binding.edtColorHex.setText("#" + envelope.hexCode)          // 현재 선택한 색상의 16진수값 표시
            selColor = envelope.color                                    // 현재 선택한 색상을 리턴하기 위해 담아둠
        })
    }


    fun setViewCloseListener(viewCloseListener: ViewCloseListener) {
        this.viewCloseListener = viewCloseListener
    }
}