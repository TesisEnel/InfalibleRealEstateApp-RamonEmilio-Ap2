package com.infaliblerealestate.presentation.login

data class LoginUiState (
    val isLoading: Boolean = false,
    val id: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val userMessage: String = ""
)