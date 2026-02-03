package com.example.mailraven.repository

import com.example.mailraven.model.LoginRequest
import com.example.mailraven.model.LoginResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class AuthRepository(private val client: HttpClient, private val settings: Settings) {
    
    companion object {
        private const val KEY_TOKEN = "auth_token"
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = client.post("auth/login") {
                setBody(LoginRequest(email, password))
            }
            val loginResponse = response.body<LoginResponse>()
            settings[KEY_TOKEN] = loginResponse.token
            Result.success(loginResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getToken(): String? = settings.getStringOrNull(KEY_TOKEN)
    
    fun isLoggedIn(): Boolean = getToken() != null
    
    fun logout() {
        settings.remove(KEY_TOKEN)
    }
}
