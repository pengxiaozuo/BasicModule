package com.peng.basic.common

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.peng.basic.util.BarUtils
import com.peng.basic.util.ToastUtils

abstract class BaseActivity : AppCompatActivity(), IBaseUi {


    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = UiConfig()
        initParams()

        configUi(config)

        configActivity(config)

        config.contentView?.let {
            initView(it, savedInstanceState)
        }

        initData()
    }

    private fun configActivity(config: UiConfig) {

        if (config.noTitle) {
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        }

        if (config.fullScreen && !config.statusBarImmersiveMode && !config.statusBarTransparentMode) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        if (config.layoutId == 0) {
            config.layoutId = getLayoutId()
        }

        if (config.contentView == null) {
            if (config.layoutId != 0) {
                setContentView(config.layoutId)
                config.contentView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
            }
        } else {
            setContentView(config.contentView)
        }

        if (config.statusColor != -1) {
            BarUtils.setBarColor(this, config.statusColor)
        }

        if (config.statusLightMode && !config.statusBarImmersiveMode && !config.statusBarTransparentMode) {
            BarUtils.setLightMode(this)
        }

        if (config.statusDarkMode && !config.statusBarImmersiveMode && !config.statusBarTransparentMode) {
            BarUtils.setDarkMode(this)
        }

        if (config.statusBarTransparentMode) {
            BarUtils.setTransparentMode(this, config.fullScreen, config.statusLightMode)
        }

        if (config.statusBarImmersiveMode) {
            BarUtils.setImmersiveMode(this, config.statusLightMode)
        }
    }

    open fun getLayoutId(): Int = 0


    open fun configUi(config: UiConfig) {}


    override fun initParams() {}


    @JvmOverloads
    fun showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
        ToastUtils.showToast(this, msg, duration)
    }

    @JvmOverloads
    fun <T : Activity> startActivity(clazz: Class<T>, bundle: Bundle? = null) {
        val intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }


    class UiConfig {

        var layoutId: Int = 0

        var contentView: View? = null

        var noTitle = false

        var fullScreen = false

        var statusLightMode = false
            set(value) {
                field = value
                if (value) {
                    statusDarkMode = false
                }
            }

        var statusDarkMode = false
            set(value) {
                field = value
                if (value) {
                    statusLightMode = false
                }
            }

        var statusColor = -1

        var statusBarTransparentMode = false
            set(value) {
                field = value
                if (value) {
                    statusBarImmersiveMode = false
                }
            }

        var statusBarImmersiveMode = false
            set(value) {
                field = value
                if (value) {
                    statusBarTransparentMode = false
                }
            }
    }


}