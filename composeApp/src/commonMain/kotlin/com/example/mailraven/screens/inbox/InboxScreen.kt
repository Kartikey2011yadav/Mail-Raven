package com.example.mailraven.screens.inbox

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.koin.getScreenModel
import com.example.mailraven.model.MessageSummary
import com.example.mailraven.screens.compose.ComposeScreen
import com.example.mailraven.screens.detail.MessageDetailScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

class InboxScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<InboxScreenModel>()
        val state by screenModel.state.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Inbox") })
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navigator.push(ComposeScreen()) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Compose")
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (state) {
                    is InboxState.Loading, InboxState.Init -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is InboxState.Success -> {
                        val messages = (state as InboxState.Success).messages
                        LazyColumn {
                            items(messages) { message ->
                                MessageItem(message) {
                                    navigator.push(MessageDetailScreen(message.id))
                                }
                                Divider()
                            }
                        }
                    }
                    is InboxState.Error -> {
                        Text(
                            text = (state as InboxState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageItem(message: MessageSummary, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = message.sender,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (!message.read) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message.receivedAt.take(10), // Simple date truncate
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = message.subject,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (!message.read) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = message.snippet,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
