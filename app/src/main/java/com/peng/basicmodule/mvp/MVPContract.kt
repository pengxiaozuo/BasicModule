package com.peng.basicmodule.mvp

import com.peng.basic.mvp.BasePresenter
import com.peng.basic.mvp.IPresenter
import com.peng.basic.mvp.IView

interface MVPContract {

    interface View : IView {
        fun getUserSuccess(user: String)

        fun getUserError(msg: String?)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun getUser()
    }

}