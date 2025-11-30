package com.infaliblerealestate.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Usuario
import com.infaliblerealestate.dominio.usecase.usuarios.GetUsuarioActualUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.LogOutUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.PutUsuarioUseCase
import com.infaliblerealestate.dominio.usecase.workerhelper.CancelSyncWorkUseCase
import com.infaliblerealestate.presentation.util.validation.UsuarioValidation
import com.infaliblerealestate.worker.WorkManagerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val CancelSyncWorkUseCase: CancelSyncWorkUseCase,
    private val getUsuarioActualUseCase: GetUsuarioActualUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val putUsuarioUseCase: PutUsuarioUseCase,
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

    fun loadUsuario() {
        viewModelScope.launch {
            getUsuarioActualUseCase().collect { usuario ->
                _state.update { state -> state.copy(
                    usuario = usuario
                ) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            CancelSyncWorkUseCase()
            logOutUseCase()
        }
    }

    fun submitUsuario(){
        viewModelScope.launch {
            _state.value.usuario?.id.let {

                _state.update { it.copy(isLoading = true,) }

                val nombreResult = validation.validateNombre(_state.value.usuarioEditado!!.nombre)
                val apellidoResult = validation.validateApellido(_state.value.usuarioEditado!!.apellido)
                val phoneNumberResult = validation.validatePhoneNumber(_state.value.usuarioEditado!!.phoneNumber)

                _state.update { it.copy(
                    nombreError = if(!nombreResult.isValid) nombreResult.errorMessage else null,
                    apellidoError = if(!apellidoResult.isValid) apellidoResult.errorMessage else null,
                    phoneNumberError = if(!phoneNumberResult.isValid) phoneNumberResult.errorMessage else null,
                    )
                }

                if(nombreResult.isValid && apellidoResult.isValid && phoneNumberResult.isValid){
                    val id = it ?: return@launch
                    when(val result = putUsuarioUseCase(id, _state.value.usuarioEditado!!)){
                        is Resource.Success -> {
                            _state.update { state -> state.copy(
                                isLoading = false,
                                userMessage = "Usuario actualizado correctamente",
                                showDialog = false,
                                usuarioEditado = null,
                            )
                            }
                        }
                        is Resource.Error -> {
                            _state.update { state -> state.copy(
                                isLoading = false,
                                userMessage = result.message,
                                showDialog = false,
                                usuarioEditado = null,
                            )
                            }
                        }
                        else -> {
                            _state.update { state -> state.copy(
                                isLoading = false,
                                userMessage = "Error al editar el Usuario",
                                showDialog = false,
                                usuarioEditado = null,
                            )
                            }
                        }

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