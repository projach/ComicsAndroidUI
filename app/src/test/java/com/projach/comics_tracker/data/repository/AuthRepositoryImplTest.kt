package com.projach.comics_tracker.data.repository

import com.projach.comics_tracker.data.source.remote.apis.AuthApi
import com.projach.comics_tracker.domain.REGISTER
import com.projach.comics_tracker.domain.dataResult.DataError
import com.projach.comics_tracker.domain.dataResult.DataResult
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AuthRepositoryImplTest {
    private lateinit var server: MockWebServer
    private lateinit var repository: AuthRepositoryImpl
    private fun createApi(client: OkHttpClient = OkHttpClient()): AuthApi =
        Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        repository = AuthRepositoryImpl(createApi())
    }

    @After
    fun tearDown() = server.shutdown()

    @Test
    fun `200 returns success`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody("""{"detail":"verification code sent"}"""))

        val result = repository.register(email = "a@b.com", username = "alice", password = "secret123")

        assertEquals(DataResult.Success(Unit), result)
    }

    @Test
    fun `sends correct method path and body`() = runTest {
        server.enqueue(MockResponse().setResponseCode(200))

        repository.register(email = "a@b.com", username = "alice", password = "secret123")

        val request = server.takeRequest()
        assertEquals("POST", request.method)
        assertEquals(REGISTER, request.path)   // adjust if REGISTER already has a leading slash
        val body = request.body.readUtf8()
        assertTrue(body.contains("\"email\":\"a@b.com\""))
        assertTrue(body.contains("\"username\":\"alice\""))
    }

    @Test
    fun `too many requests`() = runTest {
        server.enqueue(MockResponse().setResponseCode(429).setHeader("Retry-After", 60))

        val result = repository.register(email = "a@b.com", username = "alice", password = "secret123")

        assertEquals(DataResult.Failure(DataError.TooManyRequests(60)), result)
    }

    @Test
    fun `slow response maps to Timeout`() = runTest {
        val fastTimeoutClient = OkHttpClient.Builder()
            .readTimeout(200, TimeUnit.MILLISECONDS)
            .build()
        val timeoutRepository = AuthRepositoryImpl(createApi(fastTimeoutClient))
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        val result = timeoutRepository.register(email = "a@b.com", username = "alice", password = "secret123")

        assertEquals(DataResult.Failure(DataError.Timeout), result)
    }

    @Test
    fun `dropped connection maps to NoInternet`() = runTest {
        server.enqueue(MockResponse().setSocketPolicy(SocketPolicy.DISCONNECT_AT_START))

        val result = repository.register(email = "a@b.com", username = "alice", password = "secret123")

        assertEquals(DataResult.Failure(DataError.Network), result)
    }
}