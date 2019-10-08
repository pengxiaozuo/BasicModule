package com.peng.basic.mvvm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.peng.basic.base.BasicActivity
import java.lang.reflect.ParameterizedType

abstract class BasicMVVMActivity<VM : ViewModel> : BasicActivity(), IView<VM> {

    val viewModel: VM by lazy {
        initViewModel()
    }

    override fun initViewModel() =
        ViewModelProviders.of(this, getViewModelFactory()).get(getViewModelClass())
}