package com.infaliblerealestate.presentation.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.usecase.propiedades.GetPropiedadUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetPropiedadesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.compareTo
import kotlin.text.compareTo

@HiltViewModel
class CatalogoViewModel @Inject constructor(
    val getPropiedadesUseCase: GetPropiedadesUseCase,
    val getPropiedadUseCase: GetPropiedadUseCase
): ViewModel() {
    private val _state = MutableStateFlow(CatalogoUiState())
    val state: StateFlow<CatalogoUiState> = _state.asStateFlow()
    init {
        getPropiedades()
    }
    fun getPropiedades() {
        viewModelScope.launch {
            _state.value = state.value.copy(
                isLoading = true,
                infoMessage = "Cargando propiedades..."
            )
            getPropiedadesUseCase().collect { propiedades ->
                _state.update { state -> state.copy(
                    propiedades = propiedades,
                    isLoading = false,
                    infoMessage = null
                ) }
            }
        }
    }

    fun onEvent(event: CatalogoUiEvent){
        when(event){
            is CatalogoUiEvent.userMessageShown -> { _state.update { state -> state.copy(userMessage = null) } }
            is CatalogoUiEvent.hideSheet -> {_state.update { state -> state.copy(showSheet = false) }}
            is CatalogoUiEvent.loadPropiedad -> {loadPropiedad(event.id)}
            is CatalogoUiEvent.filterPropiedades -> { filterPropiedades() }
            is CatalogoUiEvent.hideFilterDialog -> {
                _state.update { it.copy(showFilterDialog = false) }
            }
            is CatalogoUiEvent.showFilterDialog -> {
                _state.update { it.copy(showFilterDialog = true) }
            }
            is CatalogoUiEvent.clearFilters -> { clearFilters() }
            is CatalogoUiEvent.onFilterCasa -> {
                _state.update { it.copy(filtroCasa = event.filter  ) }
            }
            is CatalogoUiEvent.onFilterDepartamento -> {
                _state.update { it.copy(filtroDepartamento = event.filter  ) }
            }
            is CatalogoUiEvent.onFilterVilla -> {
                _state.update { it.copy(filtroVilla = event.filter  ) }
            }
            is CatalogoUiEvent.onFilterPenthouse -> {
                _state.update { it.copy(filtroPenthouse = event.filter  ) }
            }
            is CatalogoUiEvent.onFilterSolar -> {
                _state.update { it.copy(filtroSolar = event.filter) }
            }
            is CatalogoUiEvent.onFilterLocalComercial -> {
                _state.update { it.copy(filtroLocalComercial = event.filter) }
            }
            is CatalogoUiEvent.onFilterPrecio -> {
                _state.update { it.copy(filtroPrecio = event.precio) }
            }
            is CatalogoUiEvent.onFilterHabitaciones -> {
                _state.update { it.copy(filtroHabitaciones =+ event.habitaciones) }
            }
            is CatalogoUiEvent.onFilterBanos -> {
                _state.update { it.copy(filtroBanos = event.banos) }
            }
            is CatalogoUiEvent.onFilterParqueos -> {
                _state.update { it.copy(filtroParqueos = event.parqueos) }
            }
            is CatalogoUiEvent.onFilterAlquiler -> {
                _state.update { it.copy(filtroAlquiler = event.alquiler) }
            }
            is CatalogoUiEvent.onFilterVenta -> {
                _state.update { it.copy(filtroVenta = event.venta) }
            }
            is CatalogoUiEvent.showAllFilters -> {
                _state.update { it.copy(showAllFilters = !state.value.showAllFilters) }
            }
        }
    }

    fun loadPropiedad(id: Int){
        viewModelScope.launch {
            _state.update{estate ->
                estate.copy(
                    propiedad = null,
                )
            }
            when(val propiedad = getPropiedadUseCase(id)){
                is Resource.Success -> {
                    propiedad.data?.let {
                        _state.update { state -> state.copy(
                            propiedad = it,
                            userMessage = "",
                            showSheet = true,
                            isLoading = false,
                            infoMessage = null
                            )
                        }
                    }
                }
                is Resource.Error -> {
                    _state.update { state -> state.copy(userMessage = "Error al cargar la propiedad") }
                }
                else -> {
                    _state.update { state -> state.copy(userMessage = "Error desconocido") }
                }
            }
        }
    }

    fun filterPropiedades() {
        viewModelScope.launch {
            _state.update { estate ->
                estate.copy(
                    isLoading = true,
                    infoMessage = "Aplicando filtros...",
                    showFilterDialog = false,
                )
            }

            getPropiedadesUseCase().collect { propiedadesOriginales ->
                val categoriaIds = buildSet {
                    if (_state.value.filtroCasa) add(2)
                    if (_state.value.filtroDepartamento) add(1)
                    if (_state.value.filtroVilla) add(5)
                    if (_state.value.filtroPenthouse) add(6)
                    if (_state.value.filtroSolar) add(3)
                    if (_state.value.filtroLocalComercial) add(4)
                }

                val filteredPropiedades = propiedadesOriginales.filter { propiedad ->
                    (categoriaIds.isEmpty() || propiedad.categoriaId in categoriaIds) &&
                            (_state.value.filtroPrecio <= 0.0 || propiedad.precio <= _state.value.filtroPrecio) &&
                            (_state.value.filtroHabitaciones <= 0 || propiedad.detalle.habitaciones >= _state.value.filtroHabitaciones) &&
                            (_state.value.filtroBanos <= 0 || propiedad.detalle.banos >= _state.value.filtroBanos) &&
                            (_state.value.filtroParqueos <= 0 || propiedad.detalle.parqueo >= _state.value.filtroParqueos) &&
                            (!_state.value.filtroVenta || propiedad.tipoTransaccion.equals("Venta", ignoreCase = true)) &&
                            (!_state.value.filtroAlquiler || propiedad.tipoTransaccion.equals("Alquiler", ignoreCase = true))
                }

                _state.update {
                    it.copy(
                        isLoading = false,
                        showSheet = false,
                        propiedades = filteredPropiedades,
                        showFilterDialog = false,
                        infoMessage = null
                    )
                }
            }
        }
    }

    fun clearFilters() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    filtroCasa = false,
                    filtroDepartamento = false,
                    filtroVilla = false,
                    filtroPenthouse = false,
                    filtroSolar = false,
                    filtroLocalComercial = false,
                    filtroPrecio = 0.0f,
                    filtroHabitaciones = 0,
                    filtroBanos = 0,
                    filtroParqueos = 0,
                    filtroAlquiler = false,
                    filtroVenta = false
                )
            }
        }
    }



}