package com.infaliblerealestate.data.remote.dto.propiedades.request

data class ImagenPropiedadRequest (
    val propiedadId: Int,
    val urlImagen: String,
    val orden: Int
)