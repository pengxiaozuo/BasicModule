package com.peng.basicmodule.di

import com.peng.basicmodule.api.GithubApiService
import com.peng.httputils.HttpUtils
import com.peng.httputils.create
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideGithubService(): GithubApiService {
        return HttpUtils
            .create(GithubApiService.BASE_URL)
    }
}