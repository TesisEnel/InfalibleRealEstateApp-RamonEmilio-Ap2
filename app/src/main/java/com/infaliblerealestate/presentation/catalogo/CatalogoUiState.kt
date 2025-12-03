package com.infaliblerealestate.presentation.catalogo

import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.model.Usuario

data class CatalogoUiState(
    val isLoading: Boolean = false,
    val infoMessage: String? = null,
    val usuario: Usuario? = null,
    val isAdmin: Boolean = false,
    val propiedades: List<Propiedades> = emptyList(),
    val propiedad: Propiedades? = null,
    val propiedadId: Int? = null,
    val showSheet: Boolean = false,
    val showFilterDialog: Boolean = false,
    val userMessage: String? = null,
    val filtroCasa: Boolean = false,
    val filtroDepartamento: Boolean = false,
    val filtroVilla: Boolean = false,
    val filtroPenthouse: Boolean = false,
    val filtroTerreno: Boolean = false,
    val filtroLocalComercial: Boolean = false,
    val filtroPrecio: Float = 0.0f,
    val filtroHabitaciones: Int = 0,
    val filtroBanos: Int = 0,
    val filtroParqueos: Int = 0,
    val filtroAlquiler: Boolean = false,
    val filtroVenta: Boolean = false,
    val showAllFilters: Boolean = false
)
