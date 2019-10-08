package com.peng.basic.mvvm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.peng.basic.base.BasicFragment
import java.lang.reflect.ParameterizedType

abstract class BasicMVVMFragment<VM : ViewModel> : BasicFragment(),IView<VM> {

    val viewModel: VM by lazy {
        initViewModel()
    }
    override fun initViewModel() =
        ViewModelProviders.of(this, getViewModelFactory()).get(getViewModelClass())
}