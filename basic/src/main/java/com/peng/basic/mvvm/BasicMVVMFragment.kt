package com.peng.basic.mvvm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.peng.basic.base.BasicFragment
import java.lang.reflect.ParameterizedType

abstract class BasicMVVMFragment<VM : ViewModel> : BasicFragment() {

    val viewModel: VM by lazy {
        initViewModel()
    }

    abstract fun getViewModelFactory(): ViewModelProvider.Factory

    open fun initViewModel() =
        ViewModelProviders.of(this, getViewModelFactory()).get(getViewModelClass())

    @Suppress("UNCHECKED_CAST")
    private fun getViewModelClass(): Class<VM> {
        val type: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        val actualTypeArguments = type.actualTypeArguments
        val tClass = actualTypeArguments[0]
        return tClass as Class<VM>
    }
}