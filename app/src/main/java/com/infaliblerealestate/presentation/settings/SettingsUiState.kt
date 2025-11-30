package com.infaliblerealestate.presentation.settings

import com.infaliblerealestate.dominio.model.Usuario

data class SettingsUiState(
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val usuario: Usuario? = null,
    val showDialog: Boolean = false,
    val usuarioEditado: Usuario? = null,
    val nombreError: String? = null,
    val apellidoError: String? = null,
    val phoneNumberError: String? = null,

)
