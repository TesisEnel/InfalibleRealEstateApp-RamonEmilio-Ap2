package com.infaliblerealestate.dominio.model

data class CarritoItem(
    val carritoItemId: Int,
    val carritoId: Int,
    val propiedadId: Int,
    val propiedad: Propiedades
)
