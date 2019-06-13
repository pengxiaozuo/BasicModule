package com.peng.basic.mvp

interface IPresenter {

    /**
     * 绑定view
     */
    fun takeView(view: IView)

    /**
     * 解绑view
     */
    fun dropView()
}