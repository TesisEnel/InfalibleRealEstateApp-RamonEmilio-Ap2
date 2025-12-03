package com.infaliblerealestate.data.remote.carrito

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.data.remote.dto.carrito.CarritoAddItemRequest
import com.infaliblerealestate.data.remote.dto.carrito.CarritoResponse
import javax.inject.Inject

class CarritoRemoteDataSource @Inject constructor(
    private val api: CarritoApiService
) {
    suspend fun getCarritoByUserid(id: String): Resource<CarritoResponse>{
        return try {
            val response = api.getCarritoByUserId(id)
            if(response.isSuccessful){
                response.body().let{Resource.Success(it)}
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error: ${ex.localizedMessage}")
        }
    }
    suspend fun postCarrito(id: String, item: CarritoAddItemRequest): Resource<Unit>{
        return try {
            val response = api.postCarrito(id, item)
            if(response.isSuccessful){
                Resource.Success(Unit)
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error: ${ex.localizedMessage}")
        }
    }

    suspend fun deletePropiedadDeCarrito(id: String, propiedadId: Int): Resource<Unit>{
        return try{
            val response = api.deletePropiedadDeCarrito(id, propiedadId)
            if(response.isSuccessful){
                Resource.Success(Unit)
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error: ${ex.localizedMessage}")
        }
    }


}

