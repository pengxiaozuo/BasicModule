package com.peng.basicmodule.mvp

import com.peng.basic.util.LogUtils
import javax.inject.Inject

class MVPPresenter
@Inject constructor(): MVPConceract.Presenter() {

    override fun getUser() {
        view?.getUserSuccess()
    }

}