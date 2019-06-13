package com.peng.basic.widget.banner

interface BannerIndicator {
    fun onDataChanged(list: List<Any?>?)
    fun onSelected(position: Int)
    fun onOffset(position: Int, positionOffset: Float, positionOffsetPixels: Int)
}