package com.infaliblerealestate.data.mapper

import com.infaliblerealestate.data.remote.dto.propiedades.request.ImagenPropiedadRequest
import com.infaliblerealestate.data.remote.dto.propiedades.request.PropiedadesDetalleRequest
import com.infaliblerealestate.data.remote.dto.propiedades.request.PropiedadesRequest
import com.infaliblerealestate.data.remote.dto.propiedades.response.ImagenPropiedadResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesDetalleResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesResponse
import com.infaliblerealestate.dominio.model.ImagenPropiedad
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.model.PropiedadesDetalle

fun Propiedades.toRequest(): PropiedadesRequest = PropiedadesRequest(
    titulo = titulo,
    precio = precio,
    moneda = moneda,
    ciudad = ciudad,
    estadoProvincia = estadoProvincia,
    tipoTransaccion = tipoTransaccion,
    categoriaId = categoriaId,
    fechaPublicacion = fechaPublicacion,
    fechaActualizacion = fechaActualizacion,
    estadoPropiedadId = estadoPropiedadId,
    detalle = detalle.toRequest(),
    imagenes = imagenes.map { it.toRequest() }
)

fun PropiedadesDetalle.toRequest(): PropiedadesDetalleRequest = PropiedadesDetalleRequest (
    descripcion = descripcion,
    habitaciones = habitaciones,
    banos = banos,
    parqueo = parqueo,
    metrosCuadrados = metrosCuadrados,
)

fun ImagenPropiedad.toRequest(): ImagenPropiedadRequest = ImagenPropiedadRequest(
    propiedadId = propiedadId,
    urlImagen = urlImagen,
    orden = orden
)

fun PropiedadesResponse.toDomain(): Propiedades = Propiedades(
    propiedadId = propiedadId,
    titulo = titulo,
    precio = precio,
    moneda = moneda,
    ciudad = ciudad,
    estadoProvincia = estadoProvincia,
    tipoTransaccion = tipoTransaccion,
    categoriaId = categoriaId,
    fechaPublicacion = fechaPublicacion,
    fechaActualizacion = fechaActualizacion,
    estadoPropiedadId = estadoPropiedadId,
    detalle = detalle.toDomain(),
    imagenes = imagenes.map { it.toDomain() }
)

fun PropiedadesDetalleResponse.toDomain(): PropiedadesDetalle = PropiedadesDetalle(
    propiedadId = propiedadId,
    descripcion = descripcion,
    habitaciones = habitaciones,
    banos = banos,
    parqueo = parqueo,
    metrosCuadrados = metrosCuadrados,
)

fun ImagenPropiedadResponse.toDomain(): ImagenPropiedad = ImagenPropiedad(
    imagenId = imagenId,
    propiedadId = propiedadId,
    urlImagen = urlImagen,
    orden = orden
)