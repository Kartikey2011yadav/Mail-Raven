package com.example.mailraven.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Forward
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<MessageDetailScreenModel> { parametersOf(messageId) }
        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                // Custom Clean Top Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navigator.pop() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    
                    Row {
                        IconButton(onClick = { /* Snooze */ }) {
                            Icon(Icons.Default.AccessTime, contentDescription = "Snooze")
                        }
                        IconButton(onClick = { /* Menu */ }) {
                            Icon(Icons.Default.MoreHoriz, contentDescription = "More")
                        }
                    }
                }
            },
            bottomBar = {
                if (state is MessageDetailState.Success) {
                    BottomActionBar()
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (state) {
                    is MessageDetailState.Loading, MessageDetailState.Init -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is MessageDetailState.Success -> {
                        val message = (state as MessageDetailState.Success).message
                        MessageContent(message)
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

    @Composable
    fun MessageContent(message: MessageDetail) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Subject Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = message.subject,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        lineHeight = 32.sp
                    ),
                    modifier = Modifier.weight(1f)
                )
                
                // Inbox Label Chip
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "Inbox ⌄",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sender Info
            Row(verticalAlignment = Alignment.Top) {
                // Avatar Placeholder
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Magenta) // Mock color from image
                ) {
                   // Image would go here
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = message.from.takeWhile { it != '<' }.trim(), // Just name
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = message.date.take(5), // Mock time
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Text(
                        text = "To: Me, ${message.to.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        maxLines = 1
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Body
            Text(
                text = "Hi User 👋", // Mock greeting if body doesn't have it
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message.bodyText,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 24.sp),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Looking forward to your response.",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Warm Regards,\n${message.from}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(100.dp)) // Build space for bottom bar
        }
    }

    @Composable
    fun BottomActionBar() {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 16.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Reply Button
                Button(
                    onClick = { /* Reply */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Reply, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reply")
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Forward Button
                OutlinedButton(
                    onClick = { /* Forward */ },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Icon(Icons.Default.Forward, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Forward")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconButton(onClick = { /* Emoji */ }) {
                    Icon(Icons.Default.Face, contentDescription = "React")
                }
                IconButton(onClick = { /* Star */ }) {
                    Icon(Icons.Default.StarBorder, contentDescription = "Star")
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
