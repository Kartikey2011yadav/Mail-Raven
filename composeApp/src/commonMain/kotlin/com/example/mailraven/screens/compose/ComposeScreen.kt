package com.example.mailraven.screens.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.mailraven.repository.MessageRepository
import kotlinx.coroutines.launch

class ComposeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<ComposeScreenModel>()
        val state by screenModel.state.collectAsState()

        var to by remember { mutableStateOf("") }
        var subject by remember { mutableStateOf("") }
        var body by remember { mutableStateOf("") }

        LaunchedEffect(state) {
            if (state is ComposeState.Success) {
                navigator.pop()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Compose") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { screenModel.sendMessage(to, subject, body) }) {
                            Icon(Icons.Default.Send, contentDescription = "Send")
                        }
                    }
                )
            },
            floatingActionButton = {
               // Optional: Send button here too
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
               Column(modifier = Modifier.padding(16.dp)) {
                   OutlinedTextField(
                       value = to,
                       onValueChange = { to = it },
                       label = { Text("To") },
                       modifier = Modifier.fillMaxWidth()
                   )
                   Spacer(modifier = Modifier.height(8.dp))
                   OutlinedTextField(
                       value = subject,
                       onValueChange = { subject = it },
                       label = { Text("Subject") },
                       modifier = Modifier.fillMaxWidth()
                   )
                   Spacer(modifier = Modifier.height(8.dp))
                   OutlinedTextField(
                       value = body,
                       onValueChange = { body = it },
                       label = { Text("Body") },
                       modifier = Modifier.fillMaxSize().weight(1f),
                   )
               }
               
               if (state is ComposeState.Loading) {
                   CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
               }
               
               if (state is ComposeState.Error) {
                   // Show snackbar or text
                   Text(
                       text = (state as ComposeState.Error).message,
                       color = MaterialTheme.colorScheme.error,
                       modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
                   )
               }
            }
        }
    }
}

class ComposeScreenModel(
    private val messageRepository: MessageRepository
) : StateScreenModel<ComposeState>(ComposeState.Init) {

    fun sendMessage(to: String, subject: String, body: String) {
        if (to.isBlank() || subject.isBlank() || body.isBlank()) {
            mutableState.value = ComposeState.Error("Please fill all fields")
            return
        }
        val toList = to.split(",").map { it.trim() }
        
        mutableState.value = ComposeState.Loading
        screenModelScope.launch {
            val result = messageRepository.sendMessage(toList, subject, body)
            if (result.isSuccess) {
                mutableState.value = ComposeState.Success
            } else {
                mutableState.value = ComposeState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

sealed class ComposeState {
    data object Init : ComposeState()
    data object Loading : ComposeState()
    data object Success : ComposeState()
    data class Error(val message: String) : ComposeState()
}
