package com.infaliblerealestate.data.remote.dto.carrito

import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesResponse

data class CarritoItemResponse(
    val carritoItemId: Int,
    val carritoId: Int,
    val propiedadId: Int,
    val propiedad: PropiedadesResponse
)