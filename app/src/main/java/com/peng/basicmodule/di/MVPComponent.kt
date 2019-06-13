package com.peng.basicmodule.di

import com.peng.basicmodule.mvp.MVPActivity
import dagger.Component

@Component(modules = [MVPModule::class])
interface MVPComponent {
    fun inject(activity: MVPActivity)
}