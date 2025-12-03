package com.infaliblerealestate.presentation.carrito

interface CarritoUiEvent {
    data object LoadCarrito : CarritoUiEvent
    data class DeletePropiedadDeCarrito(val propiedadId: Int) : CarritoUiEvent
    data object UserMessageShown : CarritoUiEvent
    data class LoadPropiedad(val id: Int) : CarritoUiEvent
    data object HideSheet : CarritoUiEvent
    data object ShowSheet : CarritoUiEvent
    data object SolicitarCompra : CarritoUiEvent
    data object WhatsappLaunched : CarritoUiEvent
}


