package com.example.mailraven.screens.login

import com.example.mailraven.model.LoginResponse
import com.example.mailraven.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginScreenModelTest {

    private val authRepository = mockk<AuthRepository>()
    private lateinit var screenModel: LoginScreenModel
    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
        screenModel = LoginScreenModel(authRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loginSuccessUpdatesState() = runTest(dispatcher) {
        coEvery { authRepository.login(any(), any()) } returns Result.success(LoginResponse("token", "date"))

        screenModel.login("test@example.com", "password")
        
        // Initial state Init, then Loading (async), then Success.
        // We need to advance time.
        advanceUntilIdle()

        assertTrue(screenModel.state.value is LoginState.Success)
    }

    @Test
    fun loginFailureUpdatesState() = runTest(dispatcher) {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(Exception("Error"))

        screenModel.login("test@example.com", "wrong")
        
        advanceUntilIdle()

        val state = screenModel.state.value
        assertTrue(state is LoginState.Error)
        assertEquals("Error", (state as LoginState.Error).message)
    }
}
