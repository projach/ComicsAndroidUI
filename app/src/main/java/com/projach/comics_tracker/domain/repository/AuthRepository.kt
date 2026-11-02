package com.projach.comics_tracker.domain.repository

import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult


interface AuthRepository {
    suspend fun register(email: String, password: String, username: String): DataResult<Unit, DataError>
}