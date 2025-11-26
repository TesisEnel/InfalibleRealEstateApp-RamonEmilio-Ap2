package com.infaliblerealestate.data.remote.dto.carrito

data class CarritoResponse(
    val carritoId: Int,
    val usuarioId: String,
    val items: List<CarritoItemResponse>
)