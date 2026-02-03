package com.example.mailraven.di

import com.example.mailraven.getBaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import com.example.mailraven.repository.AuthRepository
import com.example.mailraven.repository.MessageRepository
import com.example.mailraven.screens.login.LoginScreenModel
import com.example.mailraven.screens.inbox.InboxScreenModel
import com.example.mailraven.screens.detail.MessageDetailScreenModel
import com.example.mailraven.screens.compose.ComposeScreenModel

import com.russhwolf.settings.Settings

val appModule = module {
    single { Settings() }
    
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
            defaultRequest {
                url(getBaseUrl())
                contentType(ContentType.Application.Json)
            }
        }
    }
    
    single { AuthRepository(get(), get()) }
    single { MessageRepository(get(), get()) }
    
    factory { LoginScreenModel(get()) }
    factory { InboxScreenModel(get()) }
    
    factory { (messageId: String) -> MessageDetailScreenModel(messageId, get()) }
    factory { ComposeScreenModel(get()) }
}
