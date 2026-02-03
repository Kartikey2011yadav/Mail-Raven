package com.example.mailraven.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    @SerialName("expires_at")
    val expiresAt: String
)

@Serializable
data class MessageSummary(
    val id: String,
    val sender: String,
    val subject: String,
    val snippet: String,
    @SerialName("received_at")
    val receivedAt: String,
    val read: Boolean,
    val mailbox: String
)

@Serializable
data class MessageDetail(
    val id: String,
    val from: String,
    val to: List<String>,
    val subject: String,
    val date: String,
    @SerialName("body_text")
    val bodyText: String,
    @SerialName("body_html")
    val bodyHtml: String? = null
)

@Serializable
data class SendMessageRequest(
    val to: List<String>,
    val subject: String,
    val body: String
)

@Serializable
data class SendMessageResponse(
    val status: String,
    val id: String
)
