package com.daeng.okkal.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.daeng.okkal.view.MainActivity

/**
 * Created by JDY on 2023-10-13
 */
typealias Inflate2<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseDialogFragment<VB: ViewBinding>(private val inflate: Inflate2<VB>): DialogFragment() {
    private var _binding: VB? = null
    val binding get() = _binding!!
    lateinit var activity: MainActivity

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))        // 기본 배경을 투명으로 설정

        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}