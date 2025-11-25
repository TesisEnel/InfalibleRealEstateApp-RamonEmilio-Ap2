package com.infaliblerealestate.dominio.model

data class Propiedades(
    val propiedadId: Int,
    val titulo: String,
    val precio: Double,
    val moneda: String,
    val ciudad: String,
    val estadoProvincia: String,
    val tipoTransaccion: String,
    val categoriaId: Int,
    val fechaPublicacion: String,
    val fechaActualizacion: String,
    val estadoPropiedadId: Int,
    val detalle: PropiedadesDetalle,
    val imagenes: List<ImagenPropiedad>
)
