package com.peng.basicmodule.di

import com.peng.basicmodule.mvp.MVPContract
import com.peng.basicmodule.mvp.MVPPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class ActivityModule {

    @Binds
    @ActivityScope
    abstract fun bindsUserPresenter(presenter: MVPPresenter): MVPContract.Presenter
}