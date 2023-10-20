package com.daeng.okkal.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.daeng.okkal.view.dialog.LoadingDialog

typealias Inflate2<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewBinding>(private val inflate: Inflate2<VB>) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    lateinit var mActivity: MainActivity

    var loadingDialog = LoadingDialog()

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showLoading() {
        loadingDialog.show(parentFragmentManager, "loading")
    }

    fun dismissLoading() {
        loadingDialog.dismiss()
    }

}