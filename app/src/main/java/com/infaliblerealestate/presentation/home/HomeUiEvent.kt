package com.infaliblerealestate.presentation.home

import com.infaliblerealestate.dominio.model.Propiedades

interface HomeUiEvent {
    data object UserMessageShown : HomeUiEvent
    data object ShowSheet : HomeUiEvent
    data object HideSheet : HomeUiEvent
    data class CategoriaSeleccionada(val categoria: String): HomeUiEvent
    data class LoadPropiedad(val id: Int): HomeUiEvent
    data class AddToCart(val propiedad: Propiedades): HomeUiEvent
    data object LoadInitialData: HomeUiEvent
}
