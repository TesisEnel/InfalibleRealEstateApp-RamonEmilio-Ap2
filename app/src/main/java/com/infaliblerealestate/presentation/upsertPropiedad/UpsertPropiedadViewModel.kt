package com.infaliblerealestate.presentation.upsertPropiedad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.model.PropiedadesDetalle
import com.infaliblerealestate.dominio.usecase.propiedades.GetCategoriasUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetEstadoPropiedadUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.DeletePropiedadUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetPropiedadUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.SavePropiedadUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpsertPropiedadViewModel @Inject constructor(
    private val getCategoriasUseCase: GetCategoriasUseCase,
    private val getEstadoPropiedadUseCase: GetEstadoPropiedadUseCase,
    private val deletePropiedadUseCase: DeletePropiedadUseCase,
    private val savePropiedadUseCase: SavePropiedadUseCase,
    private val getPropiedadUseCase: GetPropiedadUseCase
): ViewModel() {

    private val _state = MutableStateFlow(UpsertPropiedadUiState())
    val state: StateFlow<UpsertPropiedadUiState> = _state.asStateFlow()

    init {
        loadListas()
    }

    fun onEvent(event: UpsertPropiedadUiEvent){
        when(event){
            is UpsertPropiedadUiEvent.LoadPropiedad -> {loadPropiedad(event.id)}
            is UpsertPropiedadUiEvent.SavePropiedad -> {savePropiedad()}
            is UpsertPropiedadUiEvent.ClearForm -> {clearForm()}
            is UpsertPropiedadUiEvent.OnTituloChanged -> {_state.update { it.copy(titulo = event.titulo) }}
            is UpsertPropiedadUiEvent.OnPrecioChanged -> {_state.update { it.copy(precio = event.precio) }}
            is UpsertPropiedadUiEvent.OnMonedaChanged -> {_state.update { it.copy(moneda = event.moneda) }}
            is UpsertPropiedadUiEvent.OnCiudadChanged -> {_state.update { it.copy(ciudad = event.ciudad) }}
            is UpsertPropiedadUiEvent.OnEstadoProvinciachanged -> {_state.update { it.copy(estadoProvincia = event.estadoPropiedadProvincia) }}
            is UpsertPropiedadUiEvent.OnTipoTransaccionChanged -> {_state.update { it.copy(tipoTransaccion = event.tipoTransaccion) }}
            is UpsertPropiedadUiEvent.OnCategoriaIdChanged -> {_state.update { it.copy(categoriaId = event.categoriaId, categoria = event.categoria) }}
            is UpsertPropiedadUiEvent.OnEstadoPropiedadIdChanged -> {_state.update { it.copy(estadoPropiedadId = event.estadoPropiedadId, estadoPropiedad = event.estado) }}
            is UpsertPropiedadUiEvent.OnDescripcionChanged -> {_state.update { it.copy(descripcion = event.descripcion) }}
            is UpsertPropiedadUiEvent.OnHabitacionesChanged -> {_state.update { it.copy(habitaciones = event.habitaciones) }}
            is UpsertPropiedadUiEvent.OnBanosChanged -> {_state.update { it.copy(banos = event.banos) }}
            is UpsertPropiedadUiEvent.OnParqueoChanged -> {_state.update { it.copy(parqueo = event.parqueo) }}
            is UpsertPropiedadUiEvent.OnMetrosCuadradosChanged -> {_state.update { it.copy(metrosCuadrados = event.metrosCuadrados) }}
        }
    }

    fun loadListas(){
        viewModelScope.launch {
            getCategoriasUseCase().collect { categorias ->
                _state.update { it.copy(categorias = categorias) }
            }
            getEstadoPropiedadUseCase().collect { estados ->
                _state.update { it.copy(estadosPropiedades = estados) }
            }
        }
    }

    fun loadPropiedad(id: Int){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when(val propiedad = getPropiedadUseCase(id)){
                is Resource.Success -> {
                    propiedad.data?.let{ p ->
                        _state.update { it.copy(
                            propiedadAEditar = p,
                            isEditing = true,
                            titulo = p.titulo,
                            precio = p.precio,
                            moneda = p.moneda,
                            ciudad = p.ciudad,
                            estadoProvincia = p.estadoProvincia,
                            tipoTransaccion = p.tipoTransaccion,
                            categoriaId = p.categoriaId,
                            estadoPropiedadId = p.estadoPropiedadId,
                            fechaPublicacion = p.fechaPublicacion,
                            fechaActualizacion = p.fechaActualizacion,
                            descripcion = p.detalle.descripcion,
                            habitaciones = p.detalle.habitaciones,
                            banos = p.detalle.banos,
                            parqueo = p.detalle.parqueo,
                            metrosCuadrados = p.detalle.metrosCuadrados,
                            isLoading = false
                        )}
                    }
                }

                is Resource.Error -> {
                    _state.update { it.copy(
                        isLoading = false,
                        userMessage = "Error al cargar la propiedad") }
                }

                else -> {
                    _state.update { it.copy(
                        isLoading = false,
                        userMessage = "Error desconocido") }
                }
            }
        }
    }

    fun savePropiedad() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val fecha = java.time.Instant.now().toString()

            if (_state.value.isEditing) {
                val propiedadAEditar = _state.value.propiedadAEditar!!
                val propiedadActualizada = Propiedades(
                    propiedadId = propiedadAEditar.propiedadId,
                    administradorId = propiedadAEditar.administradorId,
                    titulo = _state.value.titulo,
                    precio = _state.value.precio,
                    moneda = _state.value.moneda,
                    ciudad = _state.value.ciudad,
                    estadoProvincia = _state.value.estadoProvincia,
                    tipoTransaccion = _state.value.tipoTransaccion,
                    categoriaId = _state.value.categoriaId,
                    estadoPropiedadId = _state.value.estadoPropiedadId,
                    fechaPublicacion = propiedadAEditar.fechaPublicacion,
                    fechaActualizacion = fecha,
                    detalle = PropiedadesDetalle(
                        propiedadId = propiedadAEditar.propiedadId,
                        descripcion = _state.value.descripcion,
                        habitaciones = _state.value.habitaciones,
                        banos = _state.value.banos,
                        parqueo = _state.value.parqueo,
                        metrosCuadrados = _state.value.metrosCuadrados
                    ),
                    imagenes = emptyList()
                )

                when (val result = savePropiedadUseCase(propiedadActualizada.propiedadId, propiedadActualizada)) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userMessage = "Propiedad actualizada correctamente",
                            )
                        }
                        clearForm()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(isLoading = false, userMessage = result.message)
                        }
                    }
                    else -> {
                        _state.update {
                            it.copy(isLoading = false, userMessage = "Error al guardar la propiedad")
                        }
                    }
                }
            } else {
                val nuevaPropiedad = Propiedades(
                    propiedadId = 0,
                    titulo = _state.value.titulo,
                    precio = _state.value.precio,
                    moneda = _state.value.moneda,
                    ciudad = _state.value.ciudad,
                    estadoProvincia = _state.value.estadoProvincia,
                    tipoTransaccion = _state.value.tipoTransaccion,
                    categoriaId = _state.value.categoriaId,
                    administradorId = "3c410f08-abb4-4c5e-9dc0-93bbd000dae1",
                    estadoPropiedadId = _state.value.estadoPropiedadId,
                    fechaPublicacion = fecha,
                    fechaActualizacion = fecha,
                    detalle = PropiedadesDetalle(
                        propiedadId = 0,
                        descripcion = _state.value.descripcion,
                        habitaciones = _state.value.habitaciones,
                        banos = _state.value.banos,
                        parqueo = _state.value.parqueo,
                        metrosCuadrados = _state.value.metrosCuadrados
                    ),
                    imagenes = emptyList()
                )

                when (val result = savePropiedadUseCase(0, nuevaPropiedad)) {
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userMessage = "Propiedad guardada correctamente",
                            )
                        }
                        clearForm()
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(isLoading = false, userMessage = result.message)
                        }
                    }
                    else -> {
                        _state.update {
                            it.copy(isLoading = false, userMessage = "Error al guardar la propiedad")
                        }
                    }
                }
            }
        }
    }

    fun clearForm(){
        _state.update {
            it.copy(
                titulo = "",
                precio = 0.0,
                moneda = "",
                ciudad = "",
                estadoProvincia = "",
                tipoTransaccion = "",
                categoriaId = 0,
                estadoPropiedadId = 0,
                fechaPublicacion = "",
                fechaActualizacion = "",
                descripcion = "",
                habitaciones = 0,
                banos = 0.0,
                parqueo = 0,
                metrosCuadrados = 0.0
            )
        }
    }


}