package com.infaliblerealestate.data.mapper

import com.infaliblerealestate.data.remote.dto.carrito.CarritoAddItemRequest
import com.infaliblerealestate.data.remote.dto.carrito.CarritoItemResponse
import com.infaliblerealestate.data.remote.dto.carrito.CarritoResponse
import com.infaliblerealestate.dominio.model.Carrito
import com.infaliblerealestate.dominio.model.CarritoAddItem
import com.infaliblerealestate.dominio.model.CarritoItem

fun Carrito.toResponse(): CarritoResponse = CarritoResponse(
    carritoId = carritoId,
    usuarioId = usuarioId,
    items = items.map { it.toResponse() }
)

fun CarritoResponse.toDomain(): Carrito = Carrito(
    carritoId = carritoId,
    usuarioId = usuarioId,
    items = items.map { it.toDomain() }
)

fun CarritoItem.toResponse(): CarritoItemResponse = CarritoItemResponse(
    carritoItemId = carritoItemId,
    carritoId = carritoId,
    propiedadId = propiedadId,
    propiedad = propiedad.toResponse()
)

fun CarritoItemResponse.toDomain(): CarritoItem = CarritoItem(
    carritoItemId = carritoItemId,
    carritoId = carritoId,
    propiedadId = propiedadId,
    propiedad = propiedad.toDomain()
)

fun CarritoAddItem.toRequest(): CarritoAddItemRequest = CarritoAddItemRequest(
    propiedadId = propiedadId
)