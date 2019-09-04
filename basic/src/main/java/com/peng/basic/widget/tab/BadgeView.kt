package com.peng.basic.widget.tab

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.IntRange
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import com.peng.basic.util.dp2px

class BadgeView : TextView {
    private var _count: Int = -1
    private var _show = false
    private var _maxCount = 999
    private var _bgColor = -1
    internal var tabView: TabView? = null
    private var _emptySize = 6
    private var adjust = false

    constructor(context: Context?) : super(context) {
        setSingleLine()
        adjust = true
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    /**
     * Shows the badge with a neat little scale animation.
     */
    internal fun show() {
        _show = true
        ViewCompat.animate(this)
            .setDuration(150)
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .start()
    }

    /**
     * Hides the badge with a neat little scale animation.
     */
    internal fun hide() {
        _show = false
        ViewCompat.animate(this)
            .setDuration(150)
            .alpha(0f)
            .scaleX(0f)
            .scaleY(0f)
            .start()
    }

    /**
     * Is this badge currently visible?
     *
     * @return true is this badge is visible, otherwise false.
     */
    internal val show: Boolean get() = _show

    internal var count: Int
        get() = _count
        set(value) {
            _count = value
            if (tabView != null) {
                if (_count > 0) {
                    text = if (count <= _maxCount) count.toString() else "$_maxCount+"
                    setColoredBackground(_bgColor)
                    if (adjust) {
                        adjustSize()
                        adjustPadding()
                    }
                } else if (_count == 0) {
                    text = ""
                    setColoredBackground(_bgColor)
                    if (adjust) {
                        adjustSize()
                        adjustPadding()
                    }
                }
            }
        }

    internal var maxCount: Int
        get() = _maxCount
        set(value) {
            _maxCount = value
            if (_count != -1) {
                if (_count <= maxCount) {
                    count = _count
                }
            }
        }


    internal fun attachToTab(tab: TabView) {
        tabView = tab
        _emptySize = context.dp2px(_emptySize.toFloat()).toInt()
        _show = true
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams = params
        gravity = Gravity.CENTER
        tab.removeView(this)
        tab.addView(this)
    }

    internal fun setColoredBackground(backgroundColor: Int) {
        _bgColor = backgroundColor
        if (_bgColor == -1) return
        val innerPadding = context.dp2px(1f).toInt()
        val backgroundShape = if (text.length > 1) makeBackgroundRoundRect(
            innerPadding * 3,
            backgroundColor
        ) else makeBackgroundCircle(1, backgroundColor)
        setPadding(innerPadding, innerPadding, innerPadding, innerPadding)
        setBackgroundCompat(backgroundShape)
    }


    internal fun removeFromTab(tab: TabView) {
        tab.removeView(this)
        tabView = null
    }

    private fun adjustPadding() {
        val innerPadding = context.dp2px(1f).toInt()
        if (_count >= 10 && _show) {
            setPadding(innerPadding * 4, innerPadding, innerPadding * 4, innerPadding)
        } else if (_count > 0) {
            setPadding(innerPadding, innerPadding, innerPadding, innerPadding)
        } else {
            setPadding(0, 0, 0, 0)
        }
    }

    private fun adjustPosition() {

        tabView?.let { tab ->
            val v = tab.iconView ?: tab.titleView
            var startX = 0f
            var startY = 0f
            var xOffset = 0f
            var yOffset = height / 2f
            v?.let {
                startX = it.x
                startY = it.y
                xOffset = (it.width - (width / 2)).toFloat()
            }
            if ((xOffset + width) > tab.width) {
                xOffset = tab.width.toFloat() - width
            }
            if (xOffset < 0f) xOffset = 0f
            if ((startY - xOffset) < 0) yOffset = 0f
            x = startX + xOffset
            y = startY - yOffset
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (adjust) {
            adjustPosition()
        }
    }

    private fun adjustSize() {
        val params = layoutParams
        if (text.isEmpty()) {
            params.width = _emptySize
            params.height = _emptySize
            layoutParams = params
        } else {
            if ((width == _emptySize || height == _emptySize) && text.isNotEmpty()) {
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams = params
                if (text.length == 1) {
                    tabView?.viewTreeObserver
                        ?.addOnGlobalLayoutListener(object :
                            ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                tabView?.viewTreeObserver?.removeGlobalOnLayoutListener(this)
                                if (width != height) {
                                    val ps = layoutParams
                                    ps.width = height
                                    layoutParams = ps
                                }
                            }
                        })
                }
            } else if (text.length == 1 && width != height) {
                params.width = height
                layoutParams = params
            } else if (text.length > 1 && params.width != ViewGroup.LayoutParams.WRAP_CONTENT) {
                params.width = ViewGroup.LayoutParams.WRAP_CONTENT
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams = params
            }
        }
    }


    @SuppressLint("ObsoleteSdkInt")
    private fun setBackgroundCompat(background: Drawable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(background)
        } else {
            setBackgroundDrawable(background)
        }
    }

    private fun makeBackgroundCircle(@IntRange(from = 0) size: Int, @ColorInt color: Int): ShapeDrawable {
        val indicator = ShapeDrawable(OvalShape())
        indicator.intrinsicWidth = size
        indicator.intrinsicHeight = size
        indicator.paint.color = color
        return indicator
    }

    private fun makeBackgroundRoundRect(@IntRange(from = 0) size: Int, @ColorInt color: Int): ShapeDrawable {
        var radius = height.toFloat() / 2f
        if (radius <= 0) {
            radius = context.dp2px(20f)
        }
        val indicator = ShapeDrawable(
            RoundRectShape(
                floatArrayOf(radius, radius, radius, radius, radius, radius, radius, radius),
                RectF(),
                null
            )
        )
        indicator.intrinsicWidth = size
        indicator.intrinsicHeight = size
        indicator.paint.color = color
        return indicator
    }
}