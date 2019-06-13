package com.peng.basic.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class GridSpaceDecoration(private val spanCount: Int,
                          private val spacing: Int,
                          private val includeEdge: Boolean) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount //列

        if (parent.layoutManager.canScrollVertically()) {

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            } else {
                outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing // item top
                }
            }
        } else {
            if (includeEdge) {
                outRect.bottom = spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
                outRect.top = (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.left = spacing
                }
                outRect.right = spacing // item bottom
            } else {
                outRect.bottom = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
                outRect.top = spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.left = spacing // item top
                }
            }
        }
    }
}