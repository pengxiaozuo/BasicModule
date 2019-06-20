package com.peng.basicmodule.di

import com.peng.basic.di.ActivityScope
import com.peng.basicmodule.mvp.MVPActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(activity: MVPActivity)
}