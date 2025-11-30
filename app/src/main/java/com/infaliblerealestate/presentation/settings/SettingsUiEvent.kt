package com.infaliblerealestate.presentation.settings

interface SettingsUiEvent {
    data object LoadUsuario: SettingsUiEvent
    data object Logout: SettingsUiEvent
    data object UserMessageShown: SettingsUiEvent
    data object ShowDialog: SettingsUiEvent
    data object HideDialog: SettingsUiEvent
    data class OnNombreChanged(val nombre: String): SettingsUiEvent
    data class OnApellidoChanged(val apellido: String): SettingsUiEvent
    data class OnPhoneNumberChanged(val phoneNumber: String): SettingsUiEvent
    data object SubmitUsuario: SettingsUiEvent

}

