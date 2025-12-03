package com.infaliblerealestate.data.remote.dto.propiedades.request

data class PropiedadesRequest(
    val titulo: String,
    val precio: Double,
    val moneda: String,
    val ciudad: String,
    val estadoProvincia: String,
    val tipoTransaccion: String,
    val categoriaId: Int,
    val administradorId: String?,
    val fechaPublicacion: String,
    val fechaActualizacion: String,
    val estadoPropiedadId: Int,
    val detalle: PropiedadesDetalleRequest,
    val imagenes: List<ImagenPropiedadRequest>
)