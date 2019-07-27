package com.peng.basic.mvvm

import android.arch.lifecycle.ViewModel
import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle

abstract class BasicViewModel : ViewModel(), ILifecycle by DefaultLifecycle() {


    override fun onCleared() {
        super.onCleared()
        clear()
    }
}