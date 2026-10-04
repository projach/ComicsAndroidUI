package com.projach.comics_tracker.ui

import androidx.lifecycle.SavedStateHandle
import com.projach.comics_tracker.ComicsTrackerApplicationViewModel
import com.projach.comics_tracker.ui.screens.authCode.AuthUserViewModel
import com.projach.comics_tracker.ui.screens.registerUser.RegisterUserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RegisterUserViewModel(get()) }
    viewModel { (handle: SavedStateHandle) ->
        AuthUserViewModel(get(), handle)
    }
    viewModel { ComicsTrackerApplicationViewModel(get()) }
}