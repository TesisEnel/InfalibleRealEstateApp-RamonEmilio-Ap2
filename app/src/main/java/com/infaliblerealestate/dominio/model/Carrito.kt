package com.infaliblerealestate.dominio.model

data class Carrito(
    val carritoId: Int,
    val usuarioId: String,
    val items: List<CarritoItem>
)
