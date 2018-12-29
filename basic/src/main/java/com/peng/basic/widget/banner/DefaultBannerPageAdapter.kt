package com.peng.basic.widget.banner

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup

open class DefaultBannerPageAdapter(private var views: List<View>, private val loop: Boolean) : PagerAdapter() {

    override fun isViewFromObject(view: View, any: Any): Boolean {

        return view == any
    }

    override fun getCount(): Int {
        return if (views.isEmpty()) 0 else if (loop) Int.MAX_VALUE else views.size

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = getViewByPosition(position)
        if (view.parent != null) {
            container.removeView(view)
        }
        container.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        val view = container.getChildAt(position)
        if (view != null) {
            container.removeView(view)
        }
    }

    private fun getViewByPosition(position: Int) =
        if (views.isEmpty()) views[position] else views[position % views.size]


}