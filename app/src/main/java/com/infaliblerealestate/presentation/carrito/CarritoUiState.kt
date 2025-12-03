package com.infaliblerealestate.presentation.carrito

import com.infaliblerealestate.dominio.model.Carrito
import com.infaliblerealestate.dominio.model.CarritoItem
import com.infaliblerealestate.dominio.model.Propiedades

data class CarritoUiState(
    val isLoading: Boolean = false,
    val carrito: Carrito? = null,
    val items: List<CarritoItem> = emptyList(),
    val usuarioId: String? = null,
    val userMessage: String? = null,
    val showSheet: Boolean = false,
    val propiedad: Propiedades? = null,
    val isLoadingSheet: Boolean = false,
    val whatsappUrl: String? = null
)
