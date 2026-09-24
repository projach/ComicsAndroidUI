package com.projach.comics_tracker.networking

import com.projach.comics_tracker.data.source.remote.ServerClient
import com.projach.comics_tracker.data.source.remote.UserClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

val networkModule = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { createHttpClient(get()) }
    single { UserClient(get()) }
    single { ServerClient(get()) }
}