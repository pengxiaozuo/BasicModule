package com.peng.basicmodule.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.peng.basic.mvvm.ViewModelFactory
import com.peng.basic.mvvm.ViewModelKey
import com.peng.basicmodule.mvvm.UserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindUserViewModel(viewModel: UserViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}