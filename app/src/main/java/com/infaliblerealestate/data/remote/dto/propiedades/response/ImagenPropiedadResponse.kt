package com.infaliblerealestate.data.remote.dto.propiedades.response

data class ImagenPropiedadResponse(
    val imagenId: Int,
    val propiedadId: Int,
    val urlImagen: String,
    val orden: Int
)
