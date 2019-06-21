package com.peng.basic.widget.banner

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.peng.basic.util.logd

class BannerPageAdapter(private var views: List<View>, val loop: Boolean) : PagerAdapter() {

    var adapter: BannerView.Adapter<*>? = null

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun getCount(): Int {
        return if (views.isEmpty()) 0 else views.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = views[position]
        if (view.parent == null) {
            container.addView(view)
        }
        adapter?.onBindView(view, getRealPosition(position))
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        val view = views[position]
        container.removeView(view)
    }

    fun getRealPosition(position: Int): Int {
        if (!loop) return position
        var realCount = 0

        if (views.isNotEmpty()) {
            realCount = views.size - 2
        }

        if (realCount == 0)
            return 0
        var realPosition = (position - 1) % realCount
        if (realPosition < 0)
            realPosition += realCount

        return realPosition
    }
}