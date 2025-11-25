package com.infaliblerealestate.data.remote.dto.propiedades.response

data class PropiedadesResponse(
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
    val detalle: PropiedadesDetalleResponse,
    val imagenes: List<ImagenPropiedadResponse>
)
