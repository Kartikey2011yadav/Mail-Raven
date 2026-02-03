package com.example.mailraven.repository

import com.example.mailraven.model.MessageSummary
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MessageRepositoryTest {

    @Test
    fun getMessagesSuccess() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = """[{"id":"1","sender":"test","subject":"test","snippet":"test","received_at":"2023-01-01","read":false,"mailbox":"INBOX"}]""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
        val authRepository = mockk<AuthRepository>()
        every { authRepository.getToken() } returns "fake-token"
        
        val repository = MessageRepository(client, authRepository)

        val result = repository.getMessages()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.size)
        assertEquals("test", result.getOrNull()?.first()?.subject)
    }
}
