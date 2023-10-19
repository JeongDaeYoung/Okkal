package com.daeng.okkal.view.dialog

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import androidx.core.graphics.*
import androidx.fragment.app.DialogFragment
import com.daeng.okkal.databinding.PictureDialogBinding


class PhotoDialog(private val photo: Bitmap) : BaseDialogFragment<PictureDialogBinding>(PictureDialogBinding::inflate){
    private var viewCloseListener: ViewCloseListener? = null
    private var rgb = -1

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgPicture.setImageBitmap(photo)
        binding.imgPicture.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val viewX = event.x
                val viewY = event.y

                val viewWidth = v.width
                val viewHeight = v.height

                val imageWidth = photo.width
                val imageHeight = photo.height

                if (viewX > 0 && viewY > 0 && viewX < viewWidth && viewY < viewHeight) {

                    val imageX = viewX * (imageWidth.toDouble() / viewWidth.toDouble())
                    val imageY = viewY * (imageHeight.toDouble() / viewHeight.toDouble())

                    rgb = photo.getPixel(imageX.toInt(), imageY.toInt())
                    binding.txtColor.setBackgroundColor(rgb)
                }
            }

            return@OnTouchListener true
        })

        binding.btnOk.setOnClickListener {
            viewCloseListener!!.onCloseEvent(rgb)
            dismiss()
        }
    }

    fun setViewCloseListener(viewCloseListener: ViewCloseListener) {
        this.viewCloseListener = viewCloseListener
    }
}