package com.peng.basic.mvvm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

interface IView<VM : ViewModel> {

    fun getViewModelFactory(): ViewModelProvider.Factory?

    fun initViewModel(): VM

    @Suppress("UNCHECKED_CAST")
    fun getViewModelClass(): Class<VM> {
        val type: ParameterizedType = javaClass.genericSuperclass as ParameterizedType
        val actualTypeArguments = type.actualTypeArguments
        val tClass = actualTypeArguments[0]
        return tClass as Class<VM>
    }
}