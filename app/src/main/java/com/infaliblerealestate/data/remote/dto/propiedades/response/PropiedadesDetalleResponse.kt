package com.infaliblerealestate.data.remote.dto.propiedades.response

data class PropiedadesDetalleResponse(
    val propiedadId: Int,
    val descripcion: String,
    val habitaciones: Int,
    val banos: Double,
    val parqueo: Int,
    val metrosCuadrados: Double
)
