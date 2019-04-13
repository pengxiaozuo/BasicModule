package com.peng.basic.widget.banner

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.peng.basic.R
import com.peng.basic.util.LogUtils
import com.peng.basic.widget.banner.BannerView.Adapter.OnDataChangeListener


@SuppressLint("NewApi")
class BannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * 自动播放
     */
    private var autoPlay = true

    /**
     * 自动播放周期 单位毫秒
     */
    private var period = 5000L

    /**
     * 循环滑动
     */
    private var loop = true

    /**
     * 指示器
     */
    private var indicator: BannerIndicator? = null

    private var data: List<*>? = null

    private val views = ArrayList<View>()

    private var viewPager: ViewPager = ViewPager(context)

    lateinit var pagerAdapter: PagerAdapter

    private var adapter: Adapter? = null


    init {
        if (attrs != null) {

            val ta = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView)
            autoPlay = ta.getBoolean(R.styleable.BannerView_auto_play, autoPlay)
            loop = ta.getBoolean(R.styleable.BannerView_loop, loop)
            period = ta.getInt(R.styleable.BannerView_period, 5000).toLong()

            ta.recycle()
        }
    }

    private fun addListener() {
        viewPager.addOnPageChangeListener(onPageChangeListener)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        val vp = findViewById<ViewPager>(R.id.basic_banner_vp)
        val indicatorView = findViewById<View>(R.id.basic_banner_indicator)
        if (indicatorView is BannerIndicator) {
            indicator = indicatorView
        }
        if (vp != null) {
            viewPager = vp
        } else {
            val layoutParams = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            viewPager.layoutParams = layoutParams
            addView(viewPager, 0)
        }

        addListener()
        pagerAdapter = DefaultBannerPageAdapter(views, loop)
        viewPager.adapter = pagerAdapter
    }

    fun setAdapter(adapter: Adapter) {
        this.adapter = adapter
        this.adapter!!.addOnDataChangeListener(onDataChangeListener)
    }

    private fun startPlay() {
        if (views.size > 0) {
            mHandler.sendEmptyMessageDelayed(1, period)
        }
    }

    private fun stopPlay() {
        mHandler.removeCallbacksAndMessages(null)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopPlay()
        viewPager.removeOnPageChangeListener(onPageChangeListener)
        adapter?.removeOnDataChangeListener(onDataChangeListener)
        viewPager.adapter = null
    }

    private val mHandler = Handler(Handler.Callback {
        if (autoPlay) {
            var nextItem = viewPager.currentItem + 1
            if (nextItem >= pagerAdapter.count) {
                nextItem = 0
                viewPager.setCurrentItem(nextItem, false)
                startPlay()
            } else {
                viewPager.setCurrentItem(nextItem, true)
            }
        }
        true
    })


    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                1 -> {
                    if (autoPlay) stopPlay()
                }
                2 -> {
                    if (autoPlay && views.size > 0) startPlay()
                }
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            val p = position % (data?.size ?: 0)
            indicator?.onOffset(p, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            val p = position % (data?.size ?: 0)
            indicator?.onSelected(p)
            adapter?.onBindView(views[p], p)
        }
    }

    private val onDataChangeListener = object : OnDataChangeListener {

        override fun onDataChanged(data: List<Any>?) {

            adapter?.let { ad ->
                if (autoPlay) {
                    stopPlay()
                }
                views.clear()
                this@BannerView.data = data

                data?.let {
                    addView(it)
                }
                if (views.size > 0 && loop)
                    while (views.size < 4) {
                        data?.let {
                            addView(it)
                        }
                    }

                indicator?.onDataChanged(data)
                if (views.size > 0) {

                    indicator?.onSelected(0)
                    adapter?.onBindView(views[0], 0)
                }
                pagerAdapter.notifyDataSetChanged()
                if (views.size > 0 && loop) {
                    val currentPosition = (pagerAdapter.count / views.size / 2) * views.size
                    viewPager.currentItem = currentPosition
                }
                if (autoPlay) {
                    startPlay()
                }
            }
        }

        private fun addView(data: List<Any>) {
            adapter?.let { ad ->
                data.forEach { t ->
                    val view = ad.onCreateView(this@BannerView, t)
                    views.add(view)
                }
            }
        }
    }

    abstract class Adapter {
        var data: List<Any>? = null
        private val onDataChangeListeners = ArrayList<OnDataChangeListener>()

        abstract fun onCreateView(parent: BannerView, any: Any): View

        abstract fun onBindView(view: View, position: Int)

        fun notifyDataSetChanged() {
            onDataChangeListeners.forEach {
                it.onDataChanged(data)
            }
        }

        fun addOnDataChangeListener(listener: OnDataChangeListener) {
            onDataChangeListeners.add(listener)
        }

        fun removeOnDataChangeListener(listener: OnDataChangeListener) {
            onDataChangeListeners.remove(listener)
        }

        interface OnDataChangeListener {
            fun onDataChanged(data: List<Any>?)
        }
    }

}