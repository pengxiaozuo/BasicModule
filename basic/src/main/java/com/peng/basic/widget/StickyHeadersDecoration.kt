package com.peng.basic.widget

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class StickyHeadersDecoration(private val layoutId: Int, private var callback: StickyHeadersCallback?) :
        RecyclerView.ItemDecoration() {

    private var headerView: View? = null

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        if (headerView == null) {
            headerView = inflateHeaderView(parent)
            fixLayoutSize(headerView!!, parent)
        }


        if (callback?.isFirstSticky(pos) == true) {
            outRect.top = headerView!!.height
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val firstChild = parent.getChildAt(0)
        callback?.bindHeadView(headerView!!, parent.getChildAdapterPosition(firstChild))
        drawHeader(c, firstChild, headerView!!, true)

        for (i in 1 until parent.childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (callback?.isFirstSticky(position) == true) {
                callback?.bindHeadView(headerView!!, position)
                drawHeader(c, child, headerView!!, false)
            }
        }
    }


    private fun drawHeader(c: Canvas, child: View, headerView: View, sticky: Boolean) {
        c.save()
        if (sticky) {
            if (child.bottom <= headerView.height) {
                c.translate(0f, (child.bottom - headerView.height).toFloat())
            } else {
                c.translate(0f, 0f)
            }
//            c.translate(0f, Math.max(0, child.top - headerView.height).toFloat())
        } else {
            c.translate(0f, (child.top - headerView.height).toFloat())
        }
        headerView.draw(c)
        c.restore()
    }

    private fun inflateHeaderView(parent: RecyclerView): View {
        return LayoutInflater.from(parent.context)
                .inflate(layoutId, parent, false)
    }

    /**
     * Measures the header view to make sure its size is greater than 0 and will be drawn
     * https://yoda.entelect.co.za/view/9627/how-to-android-recyclerview-item-decorations
     */
    private fun fixLayoutSize(view: View, parent: ViewGroup) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width,
                View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height,
                View.MeasureSpec.UNSPECIFIED)

        val childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                parent.paddingLeft + parent.paddingRight,
                view.layoutParams.width)
        val childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                parent.paddingTop + parent.paddingBottom,
                view.layoutParams.height)

        view.measure(childWidth, childHeight)

        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    interface StickyHeadersCallback {
        fun isFirstSticky(position: Int): Boolean
        fun bindHeadView(view: View, position: Int)
    }
}