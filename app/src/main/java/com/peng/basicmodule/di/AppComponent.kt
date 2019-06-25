package com.peng.basicmodule.di

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import com.peng.basicmodule.MainApp
import com.peng.basicmodule.api.GithubApiService
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class, AppModule::class])
interface AppComponent {

    fun inject(application: MainApp)
    fun githubService(): GithubApiService
    fun getViewModelFactory(): ViewModelProvider.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

}