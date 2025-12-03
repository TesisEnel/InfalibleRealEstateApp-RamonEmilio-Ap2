package com.infaliblerealestate.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Login
import com.infaliblerealestate.dominio.model.Usuario
import com.infaliblerealestate.dominio.usecase.usuarios.InsertUsuarioUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.LogOutUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.ValidarCredencialesUseCase
import com.infaliblerealestate.dominio.usecase.workerhelper.ScheduleSyncWorkUseCase
import com.infaliblerealestate.worker.WorkManagerHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val validarCredencialesUseCase: ValidarCredencialesUseCase,
    val insertUsuarioUseCase: InsertUsuarioUseCase,
    val logOutUseCase: LogOutUseCase,
    var workManagerHelper: WorkManagerHelper,
    var ScheduleSyncWorkUseCase: ScheduleSyncWorkUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onEvent(event: LoginUiEvent){
        when(event){
            is LoginUiEvent.EmailChanged -> {
                _state.update { it.copy(email = event.email) }
            }
            is LoginUiEvent.PasswordChanged -> {
                _state.update {
                    it.copy(password = event.password)
                }
            }
            is LoginUiEvent.SubmitLogin -> {
                submitLogin()
            }
            is LoginUiEvent.UserMessageShown -> {
                _state.update { it.copy(userMessage = "") }
            }
        }

    }


    fun submitLogin(){
        viewModelScope.launch {
            if(state.value.email.isEmpty() || state.value.password.isEmpty()){
                _state.update{
                    it.copy(
                        emailError = "El email es requerido",
                        passwordError = "La contraseña es requerida",
                        userMessage = "Complete todos los campos"
                    )
                }
                return@launch
            }
            _state.update {
                it.copy(
                    isLoading = true,
                    emailError = null,
                    passwordError = null
                )
            }
            val login = Login(
                email = state.value.email,
                password = state.value.password
            )

            when(val result = validarCredencialesUseCase(login)){
                is Resource.Success -> {
                    result.data?.let { loginResponse ->
                        logOutUseCase()

                        insertUsuarioUseCase(
                            Usuario(
                                id = loginResponse.id,
                                userName = loginResponse.userName,
                                email = loginResponse.email,
                                phoneNumber = loginResponse.phoneNumber,
                                nombre = loginResponse.nombre,
                                apellido = loginResponse.apellido,
                                estadoUsuario = loginResponse.estadoUsuario,
                                rol = loginResponse.rol
                            )
                        )

                        ScheduleSyncWorkUseCase(loginResponse.id)

                        _state.update {
                            it.copy(
                                isLoading = false,
                                id = loginResponse.id,
                            )
                        }

                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            userMessage = result.message ?: "Error desconocido",
                            isLoading = false)
                    }
                }
                else -> {
                    _state.update {
                        it.copy(
                            userMessage = "Error desconocido",
                            isLoading = false
                        )
                    }
                }
            }
        }
    }







}
