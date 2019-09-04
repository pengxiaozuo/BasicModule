package com.peng.basic.widget.tab

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.peng.basic.R
import com.peng.basic.util.dp2px

/**
 *
 */
class TabView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val iconOnly: Int = 0
    private val titleOnly: Int = 1
    private val iconAndTitle: Int = 2

    private var _iconRes: Drawable? = null
    private var _activeIconRes: Drawable? = null
    private var _title: String = ""
    private var _inactiveAlpha: Float = 0.9f
    private var _activeAlpha: Float = 1f
    private var _inactiveColor: Int = -1
    private var _activeColor: Int = -1
    private var _activeBGColor: Int = -1
    private var _bg: Drawable? = null
    private var _badgeBackgroundColor: Int = -1
    private var _badgeTextColor: Int = -1
    private var _badgeTextSize: Int = -1
    private var _badgeMaxCount: Int = -1
    private var _badgeHidesWhenActive: Boolean = false
    private var _iconView: ImageView? = null
    private var _titleView: TextView? = null
    private var _active: Boolean = false
    private var _titleTextAppearanceResId: Int = 0
    private var _titleTypeFace: Typeface? = null
    private var _type: Int = iconOnly
    private var _badge: BadgeView? = null
    private var activePadding = 6
    private var inactivePadding = 6
    private var titleTextSize = -1
    private var iconSize = -1
    private var titleScale = 0.86f
    private var iconScale = 0.86f


    init {
        activePadding = context.dp2px(activePadding.toFloat()).toInt()
        inactivePadding = context.dp2px(inactivePadding.toFloat()).toInt()
        var layoutResId = R.layout.basic_widget_tab_default
        var isActive = false
        attrs?.let {
            val ta = getContext().obtainStyledAttributes(it, R.styleable.TabView)
            val count = ta.indexCount
            for (i in 0 until count) {
                when (val attr = ta.getIndex(i)) {
                    R.styleable.TabView_basic_tab_active -> {
                        isActive = ta.getBoolean(attr, _active)
                    }
                    R.styleable.TabView_basic_tab_title -> {
                        _title = ta.getString(attr) ?: ""
                    }
                    R.styleable.TabView_basic_tab_icon -> {
                        _iconRes = ta.getDrawable(attr)
                    }
                    R.styleable.TabView_basic_tab_active_icon -> {
                        _activeIconRes = ta.getDrawable(attr)
                    }
                    R.styleable.TabView_basic_tab_inactive_alpha -> {
                        _inactiveAlpha = ta.getFloat(attr, _inactiveAlpha)
                    }
                    R.styleable.TabView_basic_tab_active_alpha -> {
                        _activeAlpha = ta.getFloat(attr, _activeAlpha)
                    }
                    R.styleable.TabView_basic_tab_inactive_color -> {
                        _inactiveColor = ta.getColor(attr, _inactiveColor)
                    }
                    R.styleable.TabView_basic_tab_active_color -> {
                        _activeColor = ta.getColor(attr, _activeColor)
                    }
                    R.styleable.TabView_basic_tab_active_badge_bg_color -> {
                        _badgeBackgroundColor = ta.getColor(attr, -1)
                    }
                    R.styleable.TabView_basic_tab_active_badge_text_color -> {
                        _badgeTextColor = ta.getColor(attr, -1)
                    }
                    R.styleable.TabView_basic_tab_badge_hide_when_active -> {
                        _badgeHidesWhenActive = ta.getBoolean(attr, _badgeHidesWhenActive)
                    }
                    R.styleable.TabView_basic_tab_type -> {
                        _type = ta.getInt(attr, _type)
                    }
                    R.styleable.TabView_basic_tab_layout -> {
                        layoutResId = ta.getResourceId(attr, layoutResId)
                    }
                    R.styleable.TabView_basic_tab_text_appearance -> {
                        _titleTextAppearanceResId =
                            ta.getResourceId(attr, _titleTextAppearanceResId)
                    }
                    R.styleable.TabView_basic_tab_icon_size -> {
                        iconSize = ta.getDimensionPixelSize(attr, -1)
                    }
                    R.styleable.TabView_basic_tab_badge_text_size -> {
                        _badgeTextSize = ta.getDimensionPixelSize(attr, -1)
                    }
                    R.styleable.TabView_basic_tab_title_text_size -> {
                        titleTextSize = ta.getDimensionPixelSize(attr, -1)
                    }
                    R.styleable.TabView_basic_tab_active_bg_color -> {
                        _activeBGColor = ta.getColor(attr, -1)
                    }
                    R.styleable.TabView_basic_tab_title_scale -> {
                        titleScale = ta.getFloat(attr, titleScale)
                        if (titleScale > 1f) titleScale = 1f
                    }
                    R.styleable.TabView_basic_tab_icon_scale -> {
                        iconScale = ta.getFloat(attr, iconScale)
                        if (iconScale > 1f) iconScale = 1f
                    }
                    R.styleable.TabView_basic_tab_badge_max_count -> {
                        _badgeMaxCount = ta.getInt(attr, _badgeMaxCount)
                    }
                }
            }
            ta.recycle()
        }
        if (_activeIconRes == null) {
            _activeIconRes = _iconRes
        }


        prepareLayout(layoutResId)
        if (isActive) {
            select(false)
        } else {
            deselect(false)

        }
    }

    internal fun prepareLayout(layoutResId: Int) {
        View.inflate(context, layoutResId, this)
        layoutParams =
            LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
        _iconView = findViewById(R.id.basic_tab_icon)
        if (_iconRes != null) {
            _iconView?.setImageDrawable(_iconRes)
        }
        _titleView = findViewById(R.id.basic_tab_title)
        if (inActiveColor == -1) {
            _inactiveColor = titleView?.currentTextColor ?: inActiveColor
        }
        _badge = findViewById(R.id.basic_tab_badge)
        if (_badge != null) {
            _badge?.tabView = this
            initBadgeAttr(true)
            _badge?.visibility = View.GONE
        }
        when (_type) {
            iconOnly -> {
                _titleView?.visibility = View.GONE
                _iconView?.visibility = View.VISIBLE
            }
            titleOnly -> {
                _iconView?.visibility = View.GONE
                _titleView?.visibility = View.VISIBLE
            }
            iconAndTitle -> {
                _iconView?.visibility = View.VISIBLE
                _titleView?.visibility = View.VISIBLE
            }

        }
        if (titleTextSize != -1) {
            _titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize.toFloat())
        }

        if (iconSize != -1) {
            if (_iconView != null) {
                val params = _iconView!!.layoutParams
                params.width = iconSize
                params.height = iconSize
                _iconView!!.layoutParams = params
            }
        }
        _bg = background
        updateTitle()
        updateCustomTextAppearance()
        updateCustomTypeface()
        if (_iconRes == null) {
            iconView?.visibility = View.GONE
        }
    }

    private fun initBadgeAttr(customerLayout: Boolean = false) {
        _badge?.let {
            if (_badgeBackgroundColor != -1) {
                it.setColoredBackground(_badgeBackgroundColor)
            } else if (!customerLayout) {
                it.setColoredBackground(Color.RED)
            }
            if (_badgeTextColor != -1) {
                it.setTextColor(_badgeTextColor)
            } else if (!customerLayout) {
                it.setTextColor(Color.WHITE)
            }

            if (_badgeTextSize != -1) {
                it.setTextSize(TypedValue.COMPLEX_UNIT_PX, _badgeTextSize.toFloat())
            } else if (!customerLayout) {
                it.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
            }
            if (_badgeMaxCount != -1) {
                it.maxCount = _badgeMaxCount
            }
        }
    }

    private fun updateTitle() {
        _titleView?.text = _title
    }

    private fun updateCustomTextAppearance() {
        if (_titleView == null || _titleTextAppearanceResId == 0) {
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _titleView?.setTextAppearance(_titleTextAppearanceResId)
        } else {
            _titleView?.setTextAppearance(context, _titleTextAppearanceResId)
        }

    }

    private fun updateCustomTypeface() {
        if (_titleTypeFace != null && _titleView != null) {
            _titleView?.typeface = _titleTypeFace
        }
    }

    private fun updateColors(color: Int) {
        if (_titleView != null && color != -1) {
            _titleView?.setTextColor(color)
        }
    }

    private fun updateBg() {
        if (_active) {
            if (_activeBGColor != -1) setBackgroundColor(_activeBGColor)
        } else {
            if (_bg == null) {
                setBackgroundResource(0)
            } else {
                setBackgroundDrawable(_bg)
            }
        }
    }


    private fun updateAlphas(alpha: Float) {
        if (iconView != null) {
            ViewCompat.setAlpha(iconView, alpha)
        }

        if (_titleView != null) {
            ViewCompat.setAlpha(_titleView, alpha)
        }
    }


    var title: String
        get() = _title
        set(value) {
            _title = value
            updateTitle()
        }
    var titleTextAppearanceResId: Int
        get() = _titleTextAppearanceResId
        set(value) {
            _titleTextAppearanceResId = value
            updateCustomTextAppearance()
        }

    var titleTypeFace: Typeface?
        get() = _titleTypeFace
        set(value) {
            _titleTypeFace = value
            updateCustomTypeface()
        }

    val iconView: ImageView? get() = _iconView
    val titleView: TextView? get() = _titleView

    var inActiveColor: Int
        get() = _inactiveColor
        set(value) {
            _inactiveColor = value
            updateColors(_inactiveColor)
        }
    var activeColor: Int
        get() = _activeColor
        set(value) {
            _activeColor = value
            updateColors(_activeColor)
        }
    var activeAlpha: Float
        get() = _activeAlpha
        set(value) {
            _activeAlpha = value
            updateAlphas(_activeAlpha)
        }
    var inActiveAlpha: Float
        get() = _inactiveAlpha
        set(value) {
            _inactiveAlpha = value
            updateAlphas(_inactiveAlpha)
        }
    var badgeTextColor
        get() = _badgeTextColor
        set(value) {
            _badgeTextColor = value
            _badge?.setTextColor(_badgeTextColor)
        }
    var badgeBackgroundColor
        get() = _badgeBackgroundColor
        set(value) {
            _badgeBackgroundColor = value
            _badge?.setColoredBackground(value)
        }
    var badgeHidesWhenActive = _badgeHidesWhenActive
    val active: Boolean get() = _active

    var badgeCount: Int
        get() = _badge?.count ?: -1
        set(value) {
            if (value <= 0) {
                if (_badge != null) {
                    _badge!!.removeFromTab(this)
                    _badge = null
                    if (value < 0)
                        return
                }
            }

            if (_badge == null) {
                _badge = BadgeView(context)
                _badge!!.attachToTab(this)
                initBadgeAttr()
            }
            if (_badge?.visibility != View.VISIBLE) {
                _badge?.visibility = View.VISIBLE
            }
            _badge?.count = value

            if (_active && badgeHidesWhenActive) {
                _badge?.hide()
            }
        }


    fun removeBadge() {
        badgeCount = -1
    }

    fun change(select: Boolean, animate: Boolean) {
        if (select != _active) {
            if (select) {
                select(animate)
            } else {
                deselect(animate)

            }
        }
    }

    fun select(animate: Boolean = true) {
        _active = true
        if (animate) {
            val duration = 150L
            animateIconAlpha(activeAlpha, duration)
            animateTitle(activePadding, 1f, activeAlpha, duration)
            animateColors(inActiveColor, activeColor, duration)
            animateIconScale(1f, duration)
        } else {
            setTitleScale(1f)
            updateColors(activeColor)
            updateAlphas(activeAlpha)
            setIconScale(1f)
        }
        if (_activeIconRes != null) {
            _iconView?.setImageDrawable(_activeIconRes)
        }
        isSelected = true
        _badge?.let {
            if (badgeHidesWhenActive) {
                it.hide()
            }
        }
        updateBg()
    }

    fun deselect(animate: Boolean = true) {
        _active = false

        val iconPaddingTop = inactivePadding
        if (animate) {
            val duration = 150L
            animateTitle(iconPaddingTop, titleScale, inActiveAlpha, duration)
            animateIconAlpha(inActiveAlpha, duration)
            animateColors(activeColor, inActiveColor, duration)
            animateIconScale(iconScale, duration)
        } else {
            setTitleScale(titleScale)
            updateColors(inActiveColor)
            updateAlphas(inActiveAlpha)
            setIconScale(iconScale)
        }
        _iconView?.setImageDrawable(_iconRes)
        isSelected = false
        _badge?.let {
            it.show()
        }
        updateBg()
    }

    private fun animateTitle(padding: Int, scale: Float, alpha: Float, duration: Long) {
        _titleView?.let {
            ViewCompat.animate(it)
                .setDuration(duration)
                .scaleX(scale)
                .scaleY(scale)
                .alpha(alpha)
                .start()
        }
    }

    private fun animateIconScale(scale: Float, duration: Long) {
        _iconView?.let {
            ViewCompat.animate(it)
                .setDuration(duration)
                .scaleX(scale)
                .scaleY(scale)
                .start()
        }
    }

    private fun animateIconAlpha(alpha: Float, duration: Long) {
        _iconView?.let {
            ViewCompat.animate(it)
                .setDuration(duration)
                .alpha(alpha)
                .start()
        }
    }

    private fun animateColors(previousColor: Int, color: Int, duration: Long) {
        val anim = ValueAnimator()
        anim.setIntValues(previousColor, color)
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator -> updateColors(valueAnimator.animatedValue as Int) }

        anim.duration = duration
        anim.start()
    }

    private fun setTopPaddingAnimated(start: Int, end: Int, duration: Long) {

        iconView?.let { iv ->
            ValueAnimator.ofInt(start, end)
                .apply {
                    this.duration = duration
                    addUpdateListener {
                        iv.setPadding(
                            iv.paddingLeft,
                            it.animatedValue as Int,
                            iv.paddingRight,
                            iv.paddingBottom
                        )
                    }
                }
                .start()
        }
    }


    private fun setTopPadding(topPadding: Int) {

        _iconView?.setPadding(
            _iconView!!.paddingLeft,
            topPadding,
            _iconView!!.paddingRight,
            _iconView!!.paddingBottom
        )
    }

    private fun setTitleScale(scale: Float) {

        _titleView?.let {
            ViewCompat.setScaleX(it, scale)
            ViewCompat.setScaleY(it, scale)
        }
    }

    private fun setIconScale(scale: Float) {
        _iconView?.let {
            ViewCompat.setScaleX(it, scale)
            ViewCompat.setScaleY(it, scale)
        }
    }
}
