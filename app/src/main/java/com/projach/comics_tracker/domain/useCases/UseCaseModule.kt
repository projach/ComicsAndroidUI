package com.projach.comics_tracker.domain.useCases

import org.koin.dsl.module

val useCaseModule = module {
    factory { UserUseCase(get()) }
}