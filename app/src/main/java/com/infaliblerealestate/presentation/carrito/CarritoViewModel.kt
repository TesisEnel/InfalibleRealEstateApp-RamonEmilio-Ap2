package com.infaliblerealestate.presentation.carrito

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.usecase.carrito.DeletePropiedadDeCarrito
import com.infaliblerealestate.dominio.usecase.carrito.GetCarritoByIdUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetPropiedadUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.GetUsuarioActualUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val getCarritoByIdUseCase: GetCarritoByIdUseCase,
    private val getUsuarioActualUseCase: GetUsuarioActualUseCase,
    private val getPropiedadUseCase: GetPropiedadUseCase,
    private val deletePropiedadDeCarrito: DeletePropiedadDeCarrito
): ViewModel() {
    private val _state = MutableStateFlow(CarritoUiState())
    val state: StateFlow<CarritoUiState> = _state.asStateFlow()

    init {
        loadCarrito()
    }

    fun onEvent(event: CarritoUiEvent) {
        when (event) {
            is CarritoUiEvent.UserMessageShown -> {
                _state.update { it.copy(userMessage = null) }
            }
            is CarritoUiEvent.DeletePropiedadDeCarrito -> {
                deletePropiedadDeCarrito(event.propiedadId)
            }
            is CarritoUiEvent.LoadPropiedad -> {
                loadPropiedad(event.id)
            }
            is CarritoUiEvent.HideSheet -> {
                _state.update {
                    it.copy(showSheet = false)
                }
            }
            is CarritoUiEvent.ShowSheet -> {
                _state.update {
                    it.copy(showSheet = true)
                }
            }
            is CarritoUiEvent.LoadCarrito -> {
                loadCarrito()
            }

            is CarritoUiEvent.SolicitarCompra -> {
                prepararUrlWhatsapp()
            }

            is CarritoUiEvent.WhatsappLaunched -> {
                _state.update { it.copy(whatsappUrl = null) }
            }
        }
    }

    fun loadCarrito(){
        viewModelScope.launch{
            _state.update { it.copy(isLoading = true) }

            getUsuarioActualUseCase().collect { usuario ->
                _state.update { it.copy(usuarioId = usuario?.id) }

                usuario?.id?.let { userId ->
                    when(val carrito = getCarritoByIdUseCase(userId)){
                        is Resource.Success -> {
                            carrito.data?.let {
                                _state.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        carrito = it,
                                        items = it.items
                                    )
                                }
                            }
                        }
                        is Resource.Error -> {
                            _state.update { state ->
                                state.copy(
                                    isLoading = false,
                                    userMessage = carrito.message
                                )
                            }
                        }
                        else -> {
                            _state.update { state ->
                                state.copy(
                                    isLoading = false,
                                    userMessage = "Error al cargar el carrito"
                                )
                            }
                        }
                    }
                } ?: run {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun deletePropiedadDeCarrito(propiedadId: Int){
        viewModelScope.launch {

            _state.update { it.copy(isLoading = true) }

            _state.value.usuarioId.let {
                when(val result = deletePropiedadDeCarrito(it!!, propiedadId)){
                    is Resource.Success -> {
                        _state.update { state ->
                            state.copy(
                                userMessage = "Propiedad eliminada del carrito",
                            )
                        }
                        loadCarrito()
                    }
                    is Resource.Error -> {
                        _state.update { state ->
                            state.copy(
                                isLoading = false,
                                userMessage = "Error al eliminar la propiedad del carrito"
                            )
                        }
                    }
                    else -> {
                        _state.update { state ->
                            state.copy(
                                isLoading = false,
                                userMessage = "No se pudo eliminar la propiedad"
                            )
                        }
                    }
                }

            }
        }
    }


    fun loadPropiedad(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(
                showSheet = true,
                isLoadingSheet = true) }

            when(val propiedad = getPropiedadUseCase(id)){
                is Resource.Success -> {
                    propiedad.data?.let {
                        _state.update { state ->
                            state.copy(
                                showSheet = true,
                                isLoadingSheet = false,
                                propiedad = it,
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update { state ->
                        state.copy(
                            isLoadingSheet = false,
                            userMessage = propiedad.message
                        )
                    }
                }
                else -> {
                    _state.update { state ->
                        state.copy(
                            isLoadingSheet = false,
                            userMessage = "No se pudo cargar la propiedad"
                        )
                    }
                }

            }


        }
    }

    private fun prepararUrlWhatsapp() {
        val items = _state.value.items
        if (items.isEmpty()) return

        val stringBuilder = StringBuilder()
        stringBuilder.append("Hola, estoy interesado en completar la compra de las siguientes propiedades que tengo en mi carrito:\n\n")

        items.forEach { item ->
            val titulo = item.propiedad.titulo ?: "Propiedad ${item.propiedad.ciudad}"
            stringBuilder.append("- $titulo (ID: ${item.propiedad.propiedadId})\n")
        }

        stringBuilder.append("\nQuedo atento a su respuesta.")

        val mensaje = Uri.encode(stringBuilder.toString())
        val numero = "18098419551"
        val url = "https://api.whatsapp.com/send?phone=$numero&text=$mensaje"

        _state.update { it.copy(whatsappUrl = url) }
    }
}

