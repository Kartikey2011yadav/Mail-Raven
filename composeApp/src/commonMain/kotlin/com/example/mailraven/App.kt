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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import mailraven.composeapp.generated.resources.Res
import mailraven.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.KoinContext

import org.koin.compose.KoinApplication
import com.example.mailraven.di.appModule

import cafe.adriel.voyager.navigator.Navigator
import com.example.mailraven.screens.login.LoginScreen

@Composable
@Preview
fun App() {
    KoinApplication(application = {
        modules(appModule)
    }) {
        MaterialTheme {
            Navigator(LoginScreen())
        }
    }
}