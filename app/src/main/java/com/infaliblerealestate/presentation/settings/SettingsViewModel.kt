package com.infaliblerealestate.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.usecase.usuarios.GetUsuarioActualUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.LogOutUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.ScheduleSyncUsuarioUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.UpdateUsuarioLocalUseCase
import com.infaliblerealestate.dominio.usecase.workerhelper.CancelSyncWorkUseCase
import com.infaliblerealestate.presentation.util.validation.UsuarioValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val cancelSyncWorkUseCase: CancelSyncWorkUseCase,
    private val getUsuarioActualUseCase: GetUsuarioActualUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val updateUsuarioLocalUseCase: UpdateUsuarioLocalUseCase,
    private val scheduleSyncUsuarioUseCase: ScheduleSyncUsuarioUseCase,
    private val validation: UsuarioValidation
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        loadUsuario()
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.LoadUsuario -> { loadUsuario() }
            is SettingsUiEvent.Logout -> { logout() }
            is SettingsUiEvent.ShowDialog -> {showDialog()}
            is SettingsUiEvent.HideDialog -> {_state.update{it.copy(showDialog = false,usuarioEditado = null)}}
            is SettingsUiEvent.OnNombreChanged -> {_state.update{it.copy(usuarioEditado = it.usuarioEditado?.copy(nombre = event.nombre))}}
            is SettingsUiEvent.OnApellidoChanged -> {_state.update{it.copy(usuarioEditado =  it.usuarioEditado?.copy(apellido = event.apellido))}}
            is SettingsUiEvent.OnPhoneNumberChanged -> {_state.update{it.copy(usuarioEditado = it.usuarioEditado?.copy(phoneNumber = event.phoneNumber))}}
            is SettingsUiEvent.SubmitUsuario -> {submitUsuario()}
        }
    }

    private fun loadUsuario() {
        viewModelScope.launch {
            getUsuarioActualUseCase().collect { usuario ->
                _state.update {
                    it.copy(
                        usuario = usuario,
                        usuarioEditado = usuario
                    )
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            cancelSyncWorkUseCase()
            logOutUseCase()
            _state.update { SettingsUiState() }
        }
    }

    private fun submitUsuario() {
        viewModelScope.launch {
            val usuarioEditado = _state.value.usuarioEditado ?: return@launch
            val usuarioId = _state.value.usuario?.id ?: return@launch

            _state.update { it.copy(isLoading = true) }

            val nombreResult = validation.validateNombre(usuarioEditado.nombre)
            val apellidoResult = validation.validateApellido(usuarioEditado.apellido)
            val phoneNumberResult = validation.validatePhoneNumber(usuarioEditado.phoneNumber)

            val hasErrors = !nombreResult.isValid || !apellidoResult.isValid || !phoneNumberResult.isValid

            if (hasErrors) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        nombreError = nombreResult.errorMessage,
                        apellidoError = apellidoResult.errorMessage,
                        phoneNumberError = phoneNumberResult.errorMessage
                    )
                }
                return@launch
            }

            when (val result = updateUsuarioLocalUseCase(usuarioEditado)) {
                is Resource.Success -> {
                    scheduleSyncUsuarioUseCase(usuarioId)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            showDialog = false,
                            usuarioEditado = null,
                            userMessage = "Perfil actualizado correctamente"
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = result.message ?: "Error al actualizar el perfil"
                        )
                    }
                }
                else -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Error desconocido"
                        )
                    }
                }
            }
        }
    }

    fun showDialog(){
        viewModelScope.launch {
            _state.update { it.copy(
                showDialog = true,
                usuarioEditado = it.usuario,
                nombreError = null,
                apellidoError = null,
                phoneNumberError = null
                )
            }
        }
    }
}