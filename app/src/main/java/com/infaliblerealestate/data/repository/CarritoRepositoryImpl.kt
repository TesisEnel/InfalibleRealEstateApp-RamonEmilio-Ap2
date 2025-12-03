package com.infaliblerealestate.data.repository

import com.infaliblerealestate.data.mapper.toDomain
import com.infaliblerealestate.data.mapper.toRequest
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.data.remote.carrito.CarritoRemoteDataSource
import com.infaliblerealestate.dominio.model.Carrito
import com.infaliblerealestate.dominio.model.CarritoAddItem
import com.infaliblerealestate.dominio.model.CarritoItem
import com.infaliblerealestate.dominio.repository.CarritoRepository
import javax.inject.Inject

class CarritoRepositoryImpl @Inject constructor(
    private val remoteDataSource: CarritoRemoteDataSource
): CarritoRepository {
    override suspend fun getCarritoByUserid(id: String): Resource<Carrito> {
        return when(val result = remoteDataSource.getCarritoByUserid(id)){
            is Resource.Success -> {
                val carrito = result.data
                Resource.Success(carrito?.toDomain())
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> {
                Resource.Error("Error al obtener el carrito")
            }
        }

    }

    override suspend fun postCarrito(id: String, item: CarritoAddItem): Resource<Unit> {
        return when(val result = remoteDataSource.postCarrito(id, item.toRequest())){
            is Resource.Success -> {
                Resource.Success(Unit)
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> {
                Resource.Error("Error al agregar el producto al carrito")
            }
        }
    }

    override suspend fun deletePropiedadDeCarrito(id: String, propiedadId: Int): Resource<Unit> {
        return when(val result = remoteDataSource.deletePropiedadDeCarrito(id, propiedadId)){
            is Resource.Success -> {
                Resource.Success(Unit)
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
                }
            else -> {
                Resource.Error("Error al eliminar el producto del carrito")
            }
        }
    }

}