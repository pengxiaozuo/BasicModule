package com.peng.basicmodule.mvp

import com.peng.basic.mvp.BasePresenter
import com.peng.basic.mvp.IPresenter
import com.peng.basic.mvp.IView

interface MVPConceract {

    interface View : IView {
        fun getUserSuccess()

        fun getUserError()
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun getUser()
    }

}