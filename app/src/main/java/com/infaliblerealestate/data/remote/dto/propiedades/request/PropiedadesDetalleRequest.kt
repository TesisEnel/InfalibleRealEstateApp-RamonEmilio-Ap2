package com.infaliblerealestate.data.remote.dto.propiedades.request

data class PropiedadesDetalleRequest(
    val descripcion: String,
    val habitaciones: Int,
    val banos: Double,
    val parqueo: Int,
    val metrosCuadrados: Double
)