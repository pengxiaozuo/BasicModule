package com.peng.basic.widget.banner

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.PageTransformer
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.peng.basic.R
import com.peng.basic.util.logw


class BannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 自动播放
     */
    private var autoPlay = false

    /**
     * 自动播放周期 单位毫秒
     */
    private var period = 5000L

    /**
     * 循环滑动
     */
    private var loop = false

    private var indicator: BannerIndicator? = null

    private val views = ArrayList<View>()

    var viewPager: ViewPager = ViewPager(context)
        private set

    private lateinit var pagerAdapter: BannerPageAdapter

    private var adapter: Adapter<*>? = null


    init {
        if (attrs != null) {

            val ta = getContext().obtainStyledAttributes(attrs, R.styleable.BannerView)
            autoPlay = ta.getBoolean(R.styleable.BannerView_banner_auto_play, autoPlay)
            loop = ta.getBoolean(R.styleable.BannerView_banner_loop, loop)
            period = ta.getInt(R.styleable.BannerView_banner_period, 5000).toLong()

            ta.recycle()
        }
    }

    private fun addListener() {
        viewPager.removeOnPageChangeListener(onPageChangeListener)
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
            val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            viewPager.layoutParams = layoutParams
            addView(viewPager, 0)
        }

        addListener()
        pagerAdapter = BannerPageAdapter(views, loop)
        viewPager.adapter = pagerAdapter
    }

    fun <T> setAdapter(adapter: Adapter<T>) {
        this.adapter = adapter
        adapter.bannerView = this
        pagerAdapter.adapter = adapter
    }

    fun setPageTransformer(transformer: PageTransformer) {
        viewPager.setPageTransformer(true, transformer)
    }


    private fun startPlay() {
        if (views.isNotEmpty() && autoPlay) {
            stopPlay()
            postDelayed(autoPlayTask, period)
        }
    }

    private fun stopPlay() {
        removeCallbacks(autoPlayTask)
    }

    private var pre = false
    private fun switchToNextPage() {
        var nextPosition = if (pre) viewPager.currentItem - 1 else  viewPager.currentItem + 1

        if (nextPosition >= pagerAdapter.count) {
            if (loop) {
                logw("switchToNextPage fix position ${viewPager.currentItem} to 1")
                viewPager.setCurrentItem(1, false)
                nextPosition = viewPager.currentItem + 1
                viewPager.setCurrentItem(nextPosition, true)
                startPlay()
            } else if (views.size > 1) {
                pre = true
                nextPosition = viewPager.currentItem - 1
                viewPager.setCurrentItem(nextPosition, true)
            }
        } else if (nextPosition < 0) {
            pre = false
            nextPosition = viewPager.currentItem + 1
            viewPager.setCurrentItem(nextPosition, true)
        } else {
            viewPager.setCurrentItem(nextPosition, true)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startPlay()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopPlay()
    }


    private val autoPlayTask = Runnable {
        if (autoPlay) {
            switchToNextPage()
            startPlay()
        }
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                0 -> {
                    changeLoopPage()
                }
                1 -> {
                    if (autoPlay) stopPlay()

                }
                2 -> {
                    if (autoPlay && views.size > 0) startPlay()
                }
            }
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            val p = pagerAdapter.getRealPosition(position)
            indicator?.onOffset(p, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            val p = pagerAdapter.getRealPosition(position)
            indicator?.onSelected(p)
            adapter?.onPageSelected(views[p], p)
        }
    }

    fun getCurrentItem() = pagerAdapter.getRealPosition(viewPager.currentItem)

    fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        if (views.size > 0) {
            if (autoPlay) {
                startPlay()
            }
            if (loop) {
                viewPager.setCurrentItem(item + 1, smoothScroll)
            } else {
                viewPager.setCurrentItem(item, smoothScroll)
            }
        }
    }

    private fun changeLoopPage() {
        val position = viewPager.currentItem
        if (loop && views.size > 0) {
            when (position) {
                0 -> {
                    //第一张展示的是最后一张，需要跳转到真正的最后一张 lastIndex - 1
                    val index = views.lastIndex - 1
                    viewPager.setCurrentItem(index, false)
                }
                views.lastIndex -> {
                    //最后一张展示的是第一张，需要跳转到真正的第一张index = 1
                    val index = 1
                    viewPager.setCurrentItem(index + 1, false)
                    viewPager.setCurrentItem(index, false)
                }
            }
        }
    }


    private fun <T> onDataChange(data: List<T>?) {
        adapter?.let { ad ->
            ad as Adapter<T>
            stopPlay()
            viewPager.removeOnPageChangeListener(onPageChangeListener)
            //清空view
            views.clear()

            data?.let {
                addView(ad, it)
            }
            if (views.size > 0 && loop) {
                //如果循环多+2个View
                data?.let {
                    val firstData = data[0]
                    val endData = data[data.lastIndex]
                    views.add(0, ad.onCreateView(this, endData))
                    views.add(ad.onCreateView(this, firstData))

                }
            }

            indicator?.onDataChanged(data)
            if (views.size > 0) {
                indicator?.onSelected(0)
                adapter?.onPageSelected(views[0], 0)
            }
            pagerAdapter.notifyDataSetChanged()
            addListener()
            if (views.size > 0 && loop) {
                viewPager.setCurrentItem(1, false)
            } else {
                viewPager.setCurrentItem(0, false)
            }
            if (autoPlay) {
                startPlay()
            }
        }
    }

    private fun <T> addView(adapter: Adapter<T>, data: List<T>) {
        adapter.let { ad ->
            data.forEach { t ->
                val view = ad.onCreateView(this, t)
                views.add(view)
            }
        }
    }

    abstract class Adapter<T> {

        var data: List<T>? = null
        var bannerView: BannerView? = null

        abstract fun onCreateView(parent: BannerView, item: T): View

        abstract fun onBindView(view: View, position: Int)

        open fun onPageSelected(view: View, position: Int) {}

        fun notifyDataSetChanged() {
            bannerView?.onDataChange(data)
        }

    }

}