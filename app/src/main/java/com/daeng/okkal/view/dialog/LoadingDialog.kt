package com.daeng.okkal.view.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.daeng.okkal.databinding.LoadingBinding

class LoadingDialog():  BaseDialogFragment<LoadingBinding>(LoadingBinding::inflate){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        isCancelable = false
    }
}