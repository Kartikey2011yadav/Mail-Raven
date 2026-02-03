package com.example.mailraven.screens.login

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.example.mailraven.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginScreenModel(
    private val authRepository: AuthRepository
) : StateScreenModel<LoginState>(LoginState.Init) {

    fun login(email: String, pass: String) {
        mutableState.value = LoginState.Loading
        screenModelScope.launch {
            val result = authRepository.login(email, pass)
            if (result.isSuccess) {
                mutableState.value = LoginState.Success
            } else {
                mutableState.value = LoginState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }
}

sealed class LoginState {
    data object Init : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}
