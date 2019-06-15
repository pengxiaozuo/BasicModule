package com.peng.basic.mvp

interface IView {
    /**
     * 隐藏加载提示
     */
    fun hideLoading()

    /**
     * 显示加载提示
     */
    fun showLoading(msg: String? = null)

    /**
     * 显示数据为空时的提示
     */
    fun showEmptyLayout()

    /**
     * 显示错误页面
     */
    fun showErrorLayout()

    /**
     * 空数据视图的重试事件
     */
    fun onEmptyRetry() {}

    /**
     * 错误视图的重试事件
     */
    fun onErrorRetry() {}

    /**
     * 隐藏错误和空页面显示正常数据页面
     */
    fun hideEmptyAndErrorLayout()
}