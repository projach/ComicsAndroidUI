package com.projach.comics_tracker

import android.app.Application
import com.projach.comics_tracker.domain.useCases.useCaseModule
import com.projach.comics_tracker.networking.networkModule
import com.projach.comics_tracker.ui.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ComicsTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val modules = listOf(networkModule, useCaseModule, viewModelModule)

        startKoin {
            androidContext(this@ComicsTrackerApp)
            androidLogger()
            modules(modules)
        }
    }
}