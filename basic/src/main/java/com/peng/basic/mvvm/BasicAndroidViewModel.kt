package com.peng.basic.mvvm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.peng.basic.lifecycle.DefaultLifecycle
import com.peng.basic.lifecycle.ILifecycle

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application),
    ILifecycle by DefaultLifecycle() {

    override fun onCleared() {
        super.onCleared()
        clear()
    }
}