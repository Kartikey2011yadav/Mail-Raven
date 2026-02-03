package com.example.mailraven.screens.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.mailraven.model.MessageDetail
import com.example.mailraven.repository.MessageRepository
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class MessageDetailScreen(val messageId: String) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<MessageDetailScreenModel> { parametersOf(messageId) }
        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Message Detail") },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (state) {
                    is MessageDetailState.Loading, MessageDetailState.Init -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is MessageDetailState.Success -> {
                        val message = (state as MessageDetailState.Success).message
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(text = message.subject, style = MaterialTheme.typography.headlineSmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "From: ${message.from}", style = MaterialTheme.typography.titleMedium)
                            Text(text = "To: ${message.to.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = message.date, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = message.bodyText, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                    is MessageDetailState.Error -> {
                        Text(
                            text = (state as MessageDetailState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

class MessageDetailScreenModel(
    private val messageId: String,
    private val messageRepository: MessageRepository
) : StateScreenModel<MessageDetailState>(MessageDetailState.Init) {

    init {
        loadMessage()
    }

    private fun loadMessage() {
        mutableState.value = MessageDetailState.Loading
        screenModelScope.launch {
            val result = messageRepository.getMessage(messageId)
            if (result.isSuccess) {
                mutableState.value = MessageDetailState.Success(result.getOrThrow())
            } else {
                mutableState.value = MessageDetailState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

sealed class MessageDetailState {
    data object Init : MessageDetailState()
    data object Loading : MessageDetailState()
    data class Success(val message: MessageDetail) : MessageDetailState()
    data class Error(val message: String) : MessageDetailState()
}
