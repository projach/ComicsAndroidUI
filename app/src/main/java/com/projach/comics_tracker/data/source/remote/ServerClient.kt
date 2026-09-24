package com.projach.comics_tracker.data.source.remote

import com.projach.comics_tracker.domain.AWAKE_ENDPOINT
import io.ktor.client.HttpClient
import io.ktor.client.request.post

class ServerClient(private val httpClient: HttpClient) {
    suspend fun awake() {
        runCatching {
            httpClient.post(AWAKE_ENDPOINT)
        }
    }
}