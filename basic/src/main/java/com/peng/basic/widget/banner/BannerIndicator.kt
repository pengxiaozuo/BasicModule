package com.peng.basic.widget.banner

interface BannerIndicator {
    fun onDataChanged(list: List<*>?)
    fun onSelected(index: Int)
    fun onOffset(position: Int, positionOffset: Float, positionOffsetPixels: Int)
}