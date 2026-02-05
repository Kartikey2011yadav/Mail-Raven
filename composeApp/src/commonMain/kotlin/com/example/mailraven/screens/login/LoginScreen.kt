package com.example.mailraven.screens.login

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.mailraven.screens.inbox.InboxScreen

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<LoginScreenModel>()
        val state by screenModel.state.collectAsState()

        var email by remember { mutableStateOf("user@example.com") }
        var password by remember { mutableStateOf("") }
        var showLoginForm by remember { mutableStateOf(false) }

        LaunchedEffect(state) {
            if (state is LoginState.Success) {
                navigator.replace(InboxScreen())
            }
        }

        // Cloud/Sky Gradient Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background, // Light Blue / Midnight
                            MaterialTheme.colorScheme.surface     // White / Dark Surface
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .statusBarsPadding(), // Handle status bar overlap
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top: Branding
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon placeholder
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "MailRaven",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    )
                }

                // Middle: Visuals (Stacked Cards)
                // Only show these if the keyboard isn't taking up space (simplified)
                AnimatedVisibility(
                    visible = !showLoginForm,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Stacked mock cards
                        MockNotificationCard(
                            sender = "TickTick",
                            subject = "TickTick Digest",
                            snippet = "Receive a notification whenever...",
                            offsetY = 60.dp,
                            scale = 0.9f,
                            alpha = 0.6f
                        )
                        MockNotificationCard(
                            sender = "Apple",
                            subject = "Your receipt from Apple.",
                            snippet = "Receipt APPLE ID john@mac.com...",
                            offsetY = 30.dp,
                            scale = 0.95f,
                            alpha = 0.8f
                        )
                        MockNotificationCard(
                            sender = "Last Magenta",
                            subject = "[RE] Website Inquiry",
                            snippet = "Hi Ayoub, Thank you for your quick...",
                            offsetY = 0.dp,
                            scale = 1f,
                            alpha = 1f
                        )
                    }
                }

                // Bottom: Call to Action + Login Form
                Column(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    AnimatedVisibility(!showLoginForm) {
                        Column {
                            Text(
                                text = "Mail at your\nfingertips",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 44.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Make email fun again with MailRaven - built with powerful features to generate instant responses.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    AnimatedVisibility(showLoginForm) {
                        Column {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }

                    if (state is LoginState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    } else {
                        Button(
                            onClick = {
                                if (showLoginForm) {
                                    screenModel.login(email, password)
                                } else {
                                    showLoginForm = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary, // Dark Button
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                text = if (showLoginForm) "Login" else "Get started ->",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        
                        // Back button if form is shown
                        if (showLoginForm) {
                            TextButton(
                                onClick = { showLoginForm = false },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Cancel")
                            }
                        }
                    }

                    if (state is LoginState.Error) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = (state as LoginState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MockNotificationCard(
    sender: String,
    subject: String,
    snippet: String,
    offsetY: androidx.compose.ui.unit.Dp,
    scale: Float,
    alpha: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = offsetY)
            .padding(horizontal = (32 * (1 - scale)).dp) // Simulated scale effect on width
            .height(100.dp), // Fixed height for uniformity
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = alpha)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(Color.Gray))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = sender,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subject,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                maxLines = 1
            )
            Text(
                text = snippet,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
