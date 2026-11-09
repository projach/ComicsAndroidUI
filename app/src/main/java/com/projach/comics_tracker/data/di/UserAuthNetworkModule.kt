package com.projach.comics_tracker.data.di

import com.projach.comics_tracker.data.source.remote.apis.AuthApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthNetworkModule {
    @Provides
    @Singleton
    fun provideAuthApi(baseClient: OkHttpClient): AuthApi =
        baseClient.toRetrofit().create(AuthApi::class.java)
}