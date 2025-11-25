package com.infaliblerealestate.dominio.model

data class ImagenPropiedad(
    val imagenId: Int,
    val propiedadId: Int,
    val urlImagen: String,
    val orden: Int
)
