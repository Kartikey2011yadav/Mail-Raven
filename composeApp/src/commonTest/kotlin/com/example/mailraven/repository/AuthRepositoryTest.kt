package com.example.mailraven.repository

import com.example.mailraven.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AuthRepositoryTest {

    @Test
    fun loginSuccess() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """{"token":"fake-token","expires_at":"2026-02-09T10:00:00Z"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
            defaultRequest {
                url("https://example.com/api/v1/")
                contentType(ContentType.Application.Json)
            }
        }
        val repository = AuthRepository(client)

        val result = repository.login("user@example.com", "password")

        assertTrue(result.isSuccess)
        assertEquals("fake-token", result.getOrNull()?.token)
        assertEquals("fake-token", repository.getToken())
        assertTrue(repository.isLoggedIn())
    }

    @Test
    fun loginFailure() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = "Unauthorized",
                status = HttpStatusCode.Unauthorized
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json()
            }
            defaultRequest {
                url("https://example.com/api/v1/")
                contentType(ContentType.Application.Json)
            }
        }
        val repository = AuthRepository(client)

        val result = repository.login("user@example.com", "wrong")

        assertTrue(result.isFailure)
        assertEquals(null, repository.getToken())
    }
}
