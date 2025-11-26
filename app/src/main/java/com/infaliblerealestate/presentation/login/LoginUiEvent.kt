package com.infaliblerealestate.presentation.login

interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object SubmitLogin: LoginUiEvent
    data object UserMessageShown: LoginUiEvent
}