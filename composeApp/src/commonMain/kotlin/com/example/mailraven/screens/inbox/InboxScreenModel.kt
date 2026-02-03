package com.example.mailraven.screens.inbox

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.mailraven.model.MessageSummary
import com.example.mailraven.repository.MessageRepository
import kotlinx.coroutines.launch

class InboxScreenModel(
    private val messageRepository: MessageRepository
) : StateScreenModel<InboxState>(InboxState.Init) {

    fun loadMessages() {
        mutableState.value = InboxState.Loading
        screenModelScope.launch {
            val result = messageRepository.getMessages()
            if (result.isSuccess) {
                mutableState.value = InboxState.Success(result.getOrDefault(emptyList()))
            } else {
                mutableState.value = InboxState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
    
    init {
        loadMessages()
    }
}

sealed class InboxState {
    data object Init : InboxState()
    data object Loading : InboxState()
    data class Success(val messages: List<MessageSummary>) : InboxState()
    data class Error(val message: String) : InboxState()
}
