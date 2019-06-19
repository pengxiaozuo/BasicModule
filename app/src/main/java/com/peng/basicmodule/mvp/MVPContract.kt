package com.peng.basicmodule.mvp

import com.peng.basic.mvp.BasePresenter
import com.peng.basic.mvp.IView
import com.peng.basicmodule.data.User

interface MVPContract {

    interface View : IView {
        fun getUserSuccess(user: User)

        fun getUserError(msg: String?)
    }

    abstract class Presenter : BasePresenter<View>() {
        abstract fun getUser(username: String)
    }

}