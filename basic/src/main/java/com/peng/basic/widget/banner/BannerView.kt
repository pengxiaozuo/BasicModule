package com.peng.basic.widget.banner

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class BannerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet, defStyleAttr: Int)
    : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 自动播放
     */
    var isAutoPlay = true

    /**
     * 自动播放延迟时间
     */
    var delayTime = 500

    /**
     * 是否可滑动
     */
    var isScroll = true

    lateinit var viewPager: ViewPager
    lateinit var pagerAdapter: PagerAdapter

    private val data = ArrayList<Any>()
    private val views = ArrayList<View>()
    private var count = 0

    /**
     * 更新数据
     */
    fun update(list: List<Any>) {
        data.clear()
        data.addAll(list)

        views.clear()
        Integer.MAX_VALUE
        count = list.size
    }

}