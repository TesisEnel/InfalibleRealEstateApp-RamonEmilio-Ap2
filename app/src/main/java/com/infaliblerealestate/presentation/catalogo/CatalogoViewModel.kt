package com.infaliblerealestate.presentation.catalogo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.CarritoAddItem
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.usecase.carrito.PostCarritoUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetCategoriasUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetPropiedadUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetPropiedadesUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.GetUsuarioActualUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatalogoViewModel @Inject constructor(
    val getPropiedadesUseCase: GetPropiedadesUseCase,
    val getPropiedadUseCase: GetPropiedadUseCase,
    val getUsuarioActualUseCase: GetUsuarioActualUseCase,
    val postCarritoUseCase: PostCarritoUseCase,
    val getCategoriasUseCase: GetCategoriasUseCase
): ViewModel() {
    private val _state = MutableStateFlow(CatalogoUiState())
    val state: StateFlow<CatalogoUiState> = _state.asStateFlow()
    private var categoriaIds = mapOf<String, Int>()


    init {
        cargarCategorias()
        getPropiedades()
        getUsuarioActual()
    }

    fun onEvent(event: CatalogoUiEvent){
        when(event){
            is CatalogoUiEvent.userMessageShown -> { _state.update { state -> state.copy(userMessage = null) } }
            is CatalogoUiEvent.hideSheet -> {_state.update { state -> state.copy(showSheet = false) }}
            is CatalogoUiEvent.loadPropiedad -> {loadPropiedad(event.id)}
            is CatalogoUiEvent.addToCart -> { addToCart(event.propiedad) }
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
            is CatalogoUiEvent.onFilterTerreno -> {
                _state.update { it.copy(filtroTerreno = event.filter) }
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
            is CatalogoUiEvent.aplicarCategoriaInicial -> {
                aplicarCategoriaInicial(event.categoria)
            }
        }
    }

    private fun cargarCategorias() {
        viewModelScope.launch {
            getCategoriasUseCase().collect { categorias ->
                categoriaIds = categorias.associate { categoria ->
                    (categoria.nombreCategoria?.lowercase()?.trim() ?: "") to categoria.categoriaId
                }
            }
        }
    }

    private fun obtenerCategoriasSeleccionadas(): Set<Int> = buildSet {
        agregarSiActivo("casa", _state.value.filtroCasa)
        agregarSiActivo("apartamento", _state.value.filtroDepartamento)
        agregarSiActivo("villa", _state.value.filtroVilla)
        agregarSiActivo("penthouse", _state.value.filtroPenthouse)
        agregarSiActivo("terreno", _state.value.filtroTerreno)
        agregarSiActivo("local comercial", _state.value.filtroLocalComercial)
    }

    private fun MutableSet<Int>.agregarSiActivo(categoria: String, activo: Boolean) {
        if (activo) {
            categoriaIds[categoria]?.let { add(it) }
        }
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

    fun getUsuarioActual() {
        viewModelScope.launch {
            getUsuarioActualUseCase().collect { usuario ->
                _state.update { state -> state.copy(
                    usuario = usuario,
                    isAdmin = usuario?.rol?.equals("Admin", ignoreCase = true) == true
                ) }
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
            _state.update { it.copy(
                isLoading = true,
                infoMessage = "Aplicando filtros...",
                showFilterDialog = false
            )}

            getPropiedadesUseCase().collect { propiedadesOriginales ->
                val categoriaIds = obtenerCategoriasSeleccionadas()
                val filtradas = aplicarFiltros(propiedadesOriginales, categoriaIds)

                _state.update { it.copy(
                    isLoading = false,
                    showSheet = false,
                    propiedades = filtradas,
                    infoMessage = null
                )}
            }
        }
    }

    private fun aplicarFiltros(
        propiedades: List<Propiedades>,
        categoriaIds: Set<Int>
    ): List<Propiedades> {
        val state = _state.value
        return propiedades.filter { propiedad ->
            val detalle = propiedad.detalle

            (categoriaIds.isEmpty() || propiedad.categoriaId in categoriaIds) &&
                    (state.filtroPrecio <= 0.0 || propiedad.precio <= state.filtroPrecio) &&
                    (state.filtroHabitaciones <= 0 || detalle.habitaciones >= state.filtroHabitaciones) &&
                    (state.filtroBanos <= 0 || detalle.banos >= state.filtroBanos) &&
                    (state.filtroParqueos <= 0 || detalle.parqueo >= state.filtroParqueos) &&
                    (!state.filtroVenta || propiedad.tipoTransaccion.equals("Venta", ignoreCase = true)) &&
                    (!state.filtroAlquiler || propiedad.tipoTransaccion.equals("Alquiler", ignoreCase = true))
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
                    filtroTerreno = false,
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


    fun aplicarCategoriaInicial(categoria: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            if (categoriaIds.isEmpty()) {
                getCategoriasUseCase().collect { categorias ->
                    categoriaIds = categorias.associate { cat ->
                        (cat.nombreCategoria?.lowercase()?.trim() ?: "") to cat.categoriaId
                    }
                }
            }

            val categoriaKey = categoria.lowercase().trim()
            val categoriaId = categoriaIds[categoriaKey]

            if (categoriaId != null) {
                actualizarFiltroPorCategoriaId(categoriaId)
                filterPropiedades()
            } else {
                _state.update { it.copy(
                    isLoading = false,
                    userMessage = "Categoría '$categoria' no encontrada. Categorías disponibles: ${categoriaIds.keys}"
                )}
            }
        }
    }

    private fun actualizarFiltroPorCategoriaId(categoriaId: Int) {
        when (categoriaId) {
            categoriaIds["apartamento"] -> _state.update { it.copy(filtroDepartamento = true) }
            categoriaIds["casa"] -> _state.update { it.copy(filtroCasa = true) }
            categoriaIds["terreno"] -> _state.update { it.copy(filtroTerreno = true) }
            categoriaIds["local comercial"] -> _state.update { it.copy(filtroLocalComercial = true) }
            categoriaIds["villa"] -> _state.update { it.copy(filtroVilla = true) }
            categoriaIds["penthouse"] -> _state.update { it.copy(filtroPenthouse = true) }
        }
    }


    fun addToCart(propiedad: Propiedades) {
        viewModelScope.launch {
            val usuarioId = state.value.usuario?.id

            if (usuarioId == null) {
                _state.update { it.copy(userMessage = "Debe iniciar sesión para agregar al carrito") }
                return@launch
            }
            val item = CarritoAddItem(propiedadId = propiedad.propiedadId)

            when (val result = postCarritoUseCase(usuarioId, item)) {
                is Resource.Success -> {
                    _state.update { it.copy(userMessage = "Propiedad agregada al carrito") }
                }
                is Resource.Error -> {
                    _state.update { it.copy(userMessage = result.message ?: "Error al agregar al carrito") }
                }
                else -> {
                    _state.update { it.copy(userMessage = "Error desconocido") }
                }
                }

        }
    }





}