package com.infaliblerealestate.presentation.catalogo

import com.infaliblerealestate.dominio.model.Propiedades

interface CatalogoUiEvent {
    data object hideSheet: CatalogoUiEvent
    data class loadPropiedad(val id: Int): CatalogoUiEvent
    data class addToCart(val propiedad: Propiedades): CatalogoUiEvent
    data object userMessageShown: CatalogoUiEvent
    data object  hideFilterDialog: CatalogoUiEvent
    data object showFilterDialog: CatalogoUiEvent
    data object filterPropiedades: CatalogoUiEvent
    data object clearFilters: CatalogoUiEvent
    data class onFilterCasa(val filter: Boolean): CatalogoUiEvent
    data class onFilterDepartamento(val filter: Boolean): CatalogoUiEvent
    data class onFilterVilla(val filter: Boolean): CatalogoUiEvent
    data class onFilterPenthouse(val filter: Boolean): CatalogoUiEvent
    data class onFilterTerreno(val filter: Boolean): CatalogoUiEvent
    data class onFilterLocalComercial(val filter: Boolean): CatalogoUiEvent
    data class onFilterPrecio(val precio: Float): CatalogoUiEvent
    data class onFilterHabitaciones(val habitaciones: Int): CatalogoUiEvent
    data class onFilterBanos(val banos: Int): CatalogoUiEvent
    data class onFilterParqueos(val parqueos: Int): CatalogoUiEvent
    data class onFilterAlquiler(val alquiler: Boolean): CatalogoUiEvent
    data class onFilterVenta(val venta: Boolean): CatalogoUiEvent
    data object showAllFilters: CatalogoUiEvent
    data class aplicarCategoriaInicial(val categoria: String): CatalogoUiEvent

}