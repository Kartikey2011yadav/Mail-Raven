package com.example.mailraven.repository

import com.example.mailraven.model.LoginRequest
import com.example.mailraven.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthRepository(private val client: HttpClient) {
    // In a real app, strict token management is needed.
    // For now we will keep it in memory or return it.
    private var token: String? = null

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = client.post("auth/login") {
                setBody(LoginRequest(email, password))
            }
            val loginResponse = response.body<LoginResponse>()
            token = loginResponse.token
            Result.success(loginResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getToken(): String? = token
    
    fun isLoggedIn(): Boolean = token != null
    
    fun logout() {
        token = null
    }
}
