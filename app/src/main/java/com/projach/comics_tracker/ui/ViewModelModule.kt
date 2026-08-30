package com.projach.comics_tracker.ui

import com.projach.comics_tracker.ui.screens.registerUser.RegisterUserViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RegisterUserViewModel(get()) }
}