package com.infaliblerealestate.data.remote.Propiedades

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.data.remote.dto.propiedades.request.PropiedadesRequest
import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesResponse
import javax.inject.Inject

class PropiedadesRemoteDataSource @Inject constructor(
    private val api: PropiedadesApiService
) {
    suspend fun getPropiedades(): Resource<List<PropiedadesResponse>> {
        return try {
            val response = api.getPropiedades()
            if(response.isSuccessful){
                response.body().let{Resource.Success(it)}
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")
        }
    }

    suspend fun getPropiedad(id: Int): Resource<PropiedadesResponse> {
        return try {
            val response = api.getPropiedad(id)
            if(response.isSuccessful){
                response.body().let{Resource.Success(it)}
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")

        }
    }

    suspend fun putPropiedad(id: Int, propiedadesRequest: PropiedadesRequest): Resource<Unit> {
        return try {
            val response = api.putPropiedad(id, propiedadesRequest)
            if(response.isSuccessful){
                Resource.Success(Unit)
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")
        }
    }
}