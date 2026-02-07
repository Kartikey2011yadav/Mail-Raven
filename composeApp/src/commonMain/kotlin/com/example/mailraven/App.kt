package com.example.mailraven

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.mailraven.ui.theme.MailRavenTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import mailraven.composeapp.generated.resources.Res
import mailraven.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.KoinContext

import cafe.adriel.voyager.navigator.Navigator
import com.example.mailraven.screens.login.LoginScreen
import com.example.mailraven.screens.inbox.InboxScreen
import com.example.mailraven.repository.AuthRepository
import org.koin.compose.koinInject

@Composable
@Preview
fun App() {
    KoinContext {
        val authRepository = koinInject<AuthRepository>()
        val isLoggedIn = authRepository.isLoggedIn()
        
        MailRavenTheme {
            Navigator(if (isLoggedIn) InboxScreen() else LoginScreen())
        }
    }
}