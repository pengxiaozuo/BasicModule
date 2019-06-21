package com.peng.basic.widget.banner

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.peng.basic.R
import com.peng.basic.util.SizeUtils


class DefaultBannerIndicator @JvmOverloads constructor(
    context: Context,
    attrSets: AttributeSet? = null,
    defStyleAttrs: Int = 0
) : View(context, attrSets, defStyleAttrs), BannerIndicator {

    private var doViewWith = 6f
    private var doViewHeight = doViewWith
    private var doViewMargin = doViewWith
    private var normalColor = 0xFFcccccc.toInt()
    private var selectedColor = 0xFF4A68F1.toInt()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var count = 0
    private var selected = 0
    private var strokeWidth = 1f
    private var list = mutableListOf<RectF>()
    /**
     * Shape is a rectangle, possibly with rounded corners
     */
    val RECTANGLE = 0

    /**
     * Shape is an ellipse
     */
    val OVAL = 1

    /**
     * Shape is a ring.
     */
    val RING = 3


    private var shape = OVAL

    private var cornerRadius = 0f

    init {
        doViewWith = SizeUtils.dp2px(context, doViewWith)
        doViewHeight = doViewWith
        doViewMargin = doViewWith

        attrSets?.let {
            val ta = getContext().obtainStyledAttributes(attrSets, R.styleable.DefaultBannerIndicator)
            val taCount = ta.indexCount
            for (i in 0 until taCount) {
                when (val attr = ta.getIndex(i)) {
                    R.styleable.DefaultBannerIndicator_banner_indicator_color -> {
                        normalColor = ta.getColor(attr, normalColor)
                    }
                    R.styleable.DefaultBannerIndicator_banner_indicator_select_color -> {
                        selectedColor = ta.getColor(attr, selectedColor)
                    }
                    R.styleable.DefaultBannerIndicator_banner_indicator_margin -> {
                        doViewMargin = ta.getDimensionPixelSize(attr, doViewMargin.toInt()).toFloat()
                    }
                    R.styleable.DefaultBannerIndicator_banner_indicator_width -> {
                        doViewWith = ta.getDimensionPixelSize(attr, doViewWith.toInt()).toFloat()
                    }
                    R.styleable.DefaultBannerIndicator_banner_indicator_height -> {
                        doViewHeight = ta.getDimensionPixelSize(attr, doViewHeight.toInt()).toFloat()
                    }
                    R.styleable.DefaultBannerIndicator_android_shape -> {
                        shape = ta.getInt(attr, OVAL)
                    }
                    R.styleable.DefaultBannerIndicator_banner_indicator_corner_radius -> {
                        cornerRadius = ta.getDimensionPixelSize(attr, cornerRadius.toInt()).toFloat()
                    }
                    R.styleable.DefaultBannerIndicator_banner_indicator_ring_width -> {
                        strokeWidth = ta.getDimensionPixelSize(attr, strokeWidth.toInt()).toFloat()
                    }
                }
            }

            ta.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)

        var height = MeasureSpec.getSize(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST) {
            width = (count * doViewMargin + count * doViewWith).toInt()
        }

        if (heightMode == MeasureSpec.AT_MOST) {
            height = (doViewHeight + doViewMargin).toInt()
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        initRectFList()
    }


    override fun onDataChanged(list: List<Any?>?) {
        count = list?.size ?: 0
        requestLayout()
    }

    private fun initRectFList() {
        list.clear()
        val cy = height / 2f
        repeat(count) {
            val cx = getDoCX(it)
            val cf = RectF(
                cx - doViewWith / 2,
                cy - doViewHeight / 2,
                cx + doViewWith / 2,
                cy + doViewHeight / 2
            )
            list.add(cf)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        repeat(count) {
            if (it == selected) {
                paint.color = selectedColor
            } else {
                paint.color = normalColor
            }
            val cf = list[it]
            when (shape) {
                OVAL -> {
                    paint.style = Paint.Style.FILL
                    canvas?.drawOval(cf, paint)
                }
                RECTANGLE -> {
                    paint.style = Paint.Style.FILL
                    canvas?.drawRoundRect(cf, cornerRadius, cornerRadius, paint)
                }
                RING -> {
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = strokeWidth
                    canvas?.drawOval(cf, paint)
                }
            }
        }
    }

    private fun getDoCX(index: Int): Float {
        val first = doViewMargin / 2f + doViewWith / 2f
        val space = doViewMargin + doViewWith
        return first + space * index
    }


    override fun onSelected(position: Int) {
        selected = position
        invalidate()
    }


    override fun onOffset(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }
}