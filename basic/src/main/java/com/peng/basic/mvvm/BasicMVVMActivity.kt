package com.peng.basic.mvvm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.peng.basic.base.BasicActivity
import java.lang.reflect.ParameterizedType

abstract class BasicMVVMActivity<VM : ViewModel> : BasicActivity() {

    val viewModel: VM by lazy {
        initViewModel()
    }

    abstract fun getViewModelFactory(): ViewModelProvider.Factory

    open fun initViewModel() = ViewModelProviders.of(this, getViewModelFactory()).get(getViewModelClass())

    private fun getViewModelClass(): Class<VM> {
        val type: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        val actualTypeArguments = type.actualTypeArguments
        val tClass = actualTypeArguments[0]
        return tClass as Class<VM>
    }
}