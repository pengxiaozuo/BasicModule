package com.peng.basic.widget.banner

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.peng.basic.util.SizeUtils


class DefaultBannerIndicator : LinearLayout, BannerIndicator {

    private var doViewWith = 6f
    private val normalDoBmp = createBitmap(0xFFcccccc.toInt())
    private val selectedDoBmp = createBitmap(0xFF4A68F1.toInt())

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        doViewWith = SizeUtils.dp2px(context, doViewWith)
    }


    override fun onDataChanged(list: List<Any>?) {
        removeAllViews()
        list?.forEach {
            addDoView()
        }
    }

    private fun addDoView() {
        val doView = ImageView(context)
        val params = LinearLayout.LayoutParams(doViewWith.toInt(), doViewWith.toInt())
        val margin = SizeUtils.dp2px(context, 8f).toInt()
        params.rightMargin = margin
        params.leftMargin = margin
        params.topMargin = margin
        params.bottomMargin = margin
        doView.setImageBitmap(normalDoBmp)
        addView(doView, params)
    }

    private fun createBitmap(color: Int): Bitmap {
        val bmp = Bitmap.createBitmap(doViewWith.toInt(), doViewWith.toInt(), Bitmap.Config.ARGB_4444)
        val canvas = Canvas(bmp)
        val paint = Paint()
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = color
        canvas.drawCircle(bmp.width.toFloat() / 2, bmp.width.toFloat() / 2, bmp.width.toFloat() / 2, paint)
        return bmp
    }

    override fun onSelected(position: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child as ImageView
            if (i == position) {
                child.setImageBitmap(selectedDoBmp)
            } else {
                child.setImageBitmap(normalDoBmp)
            }
        }
    }

    override fun onOffset(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        normalDoBmp.recycle()
        selectedDoBmp.recycle()
    }

}