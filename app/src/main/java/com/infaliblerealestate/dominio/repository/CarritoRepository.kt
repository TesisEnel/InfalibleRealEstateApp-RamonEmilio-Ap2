package com.infaliblerealestate.dominio.repository

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Carrito

interface CarritoRepository {
    suspend fun getCarritoByUserid(id: String): Resource<Carrito>
    suspend fun postCarrito(id: String, propiedadId: Int): Resource<Unit>
}
