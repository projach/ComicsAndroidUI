package com.projach.comics_tracker.data.di

import com.projach.comics_tracker.data.network.AuthInterceptor
import com.projach.comics_tracker.data.network.UnauthorizedInterceptor
import com.projach.comics_tracker.data.source.remote.apis.ComicsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ComicsNetworkModule {

    @Provides
    @Singleton
    fun provideComicsApi(
        baseClient: OkHttpClient,
        auth: AuthInterceptor,
        unauthorized: UnauthorizedInterceptor
    ): ComicsApi {
        val client = baseClient.newBuilder()
            .addInterceptor(unauthorized)
            .addInterceptor(auth)
            .build()
        return client.toRetrofit().create(ComicsApi::class.java)
    }
}