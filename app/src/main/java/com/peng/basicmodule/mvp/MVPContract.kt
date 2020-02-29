package com.peng.basicmodule.mvp

import com.peng.basic.mvp.BasicPresenter
import com.peng.basic.mvp.IView
import com.peng.basicmodule.data.User

interface MVPContract {

    interface View : IView {
        fun getUserSuccess(user: User)

        fun getUserError(msg: String?)

        fun showLoading(msg: String?)

        fun hideLoading()
    }

    abstract class Presenter : BasicPresenter<View>() {
        abstract fun getUser(username: String)
    }

}