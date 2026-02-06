package com.example.mailraven.repository

import com.example.mailraven.model.MessageDetail
import com.example.mailraven.model.MessageSummary
import com.example.mailraven.model.SendMessageRequest
import com.example.mailraven.model.SendMessageResponse
import com.example.mailraven.model.MessagesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class MessageRepository(
    private val client: HttpClient,
    private val authRepository: AuthRepository
) {
    private fun getToken(): String {
        return authRepository.getToken() ?: throw IllegalStateException("Not logged in")
    }

    suspend fun getMessages(limit: Int = 50, offset: Int = 0): Result<List<MessageSummary>> {
        return try {
            val response = client.get("messages") {
                bearerAuth(getToken())
                parameter("limit", limit)
                parameter("offset", offset)
            }.body<MessagesResponse>()
            Result.success(response.messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMessage(id: String): Result<MessageDetail> {
        return try {
            val message = client.get("messages/$id") {
                bearerAuth(getToken())
            }.body<MessageDetail>()
            Result.success(message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendMessage(to: List<String>, subject: String, body: String): Result<SendMessageResponse> {
        return try {
            val response = client.post("messages/send") {
                bearerAuth(getToken())
                setBody(SendMessageRequest(to, subject, body))
            }.body<SendMessageResponse>()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
