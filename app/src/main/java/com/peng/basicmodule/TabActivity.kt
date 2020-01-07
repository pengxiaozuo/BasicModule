package com.peng.basicmodule

import android.os.Bundle
import android.view.View
import com.peng.basic.base.BasicActivity
import com.peng.basic.util.click
import com.peng.basic.util.logd
import com.peng.basic.util.toMain
import com.peng.basic.widget.tab.TabView
import io.reactivex.Flowable
import kotlinx.android.synthetic.main.activity_tab.*
import java.util.concurrent.TimeUnit

class TabActivity : BasicActivity() {
    override fun initView(view: View, savedInstanceState: Bundle?) {
        tab1.click {
            select(tab1)
        }
        tab2.click {
            select(tab2)
        }
        tab3.click {
            select(tab3)
        }
        tab4.click {
            select(tab4)
        }
        tab5.click {
            select(tab5)
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        tab2.badgeCount = 2
        tab2.badgeHidesWhenActive = true
        tab1.select(false)

        tab3.badgeCount = 0
        tab3.badgeHidesWhenActive = false
        tab5.badgeHidesWhenActive = true
        tab5.badgeCount = 20

        tab6.badgeCount = 99
        var count = -1
        var add = true
        Flowable.interval(1, TimeUnit.SECONDS)
            .toMain()
            .subscribe({
                if (count >= 10) {
                    add = false
                } else if (count < -2) {
                    add = true
                }
                if (add) {
                    count++
                } else {
                    count--
                }
                tab7.badgeCount = count
            }, {
                it.printStackTrace()
            }).add()
    }

    override fun getLayoutId() = R.layout.activity_tab

    fun select(tabView: TabView) {
        if (tab1.active) {
            tab1.deselect(true)
        }
        if (tab2.active) {
            tab2.deselect(true)
        }
        if (tab3.active) {
            tab3.deselect(true)
        }
        if (tab4.active) {
            tab4.deselect(true)
        }
        if (tab5.active) {
            tab5.deselect(true)
        }

        tabView.select(true)
    }
}