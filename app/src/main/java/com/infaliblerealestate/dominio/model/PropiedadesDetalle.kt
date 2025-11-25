package com.infaliblerealestate.dominio.model

data class PropiedadesDetalle(
    val propiedadId: Int,
    val descripcion: String,
    val habitaciones: Int,
    val banos: Double,
    val parqueo: Int,
    val metrosCuadrados: Double
)
