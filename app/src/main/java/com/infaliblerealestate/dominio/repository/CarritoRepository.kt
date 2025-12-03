package com.infaliblerealestate.dominio.repository

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Carrito
import com.infaliblerealestate.dominio.model.CarritoAddItem

interface CarritoRepository {
    suspend fun getCarritoByUserid(id: String): Resource<Carrito>
    suspend fun postCarrito(id: String, item: CarritoAddItem): Resource<Unit>
    suspend fun deletePropiedadDeCarrito(id: String, propiedadId: Int): Resource<Unit>
}
