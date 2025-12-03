package com.infaliblerealestate.data.remote.propiedades

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.data.remote.dto.propiedades.request.PropiedadesRequest
import com.infaliblerealestate.data.remote.dto.propiedades.response.CategoriaResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.EstadoPropiedadResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.UbicacionResponse
import okhttp3.MultipartBody
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

    suspend fun postPropiedad(propiedad: PropiedadesRequest): Resource<PropiedadesResponse>{
        return try{
            val response = api.postPropiedad(propiedad)
            if(response.isSuccessful){
                response.body().let{Resource.Success(it)}
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")
        }
    }

    suspend fun deletePropiedad(id: Int): Resource<Unit>{
        return try{
            val response = api.deletePropiedad(id)
            if(response.isSuccessful){
                Resource.Success(Unit)
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")
        }
    }

    suspend fun getCategorias(): Resource<List<CategoriaResponse>>{
        return try{
            val response = api.getCategorias()
            if(response.isSuccessful){
                response.body().let { Resource.Success(it) }
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")
        }
    }

    suspend fun getEstadoPropiedades(): Resource<List<EstadoPropiedadResponse>>{
        return try{
            val response = api.getEstadoPropiedades()
            if(response.isSuccessful){
                response.body().let { Resource.Success(it) }
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")
        }
    }

    suspend fun uploadImages(propiedadId: Int, images: List<MultipartBody.Part>): Resource<Unit>{
        return try{
            val response = api.uploadImages(propiedadId, images)
            if(response.isSuccessful){
                Resource.Success(Unit)
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")
        }
    }

    suspend fun deleteImages(imagenesIds: List<Int>): Resource<Unit>{
        return try{
            val response = api.deleteImages(imagenesIds)
            if(response.isSuccessful){
                Resource.Success(Unit)
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")
        }
    }

    suspend fun getUbicaciones(): Resource<UbicacionResponse>{
        return try{
            val response = api.getUbicaciones()
            if(response.isSuccessful){
                response.body().let { Resource.Success(it) }
            }else{
                Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (ex: Exception){
            Resource.Error("Error ${ex.localizedMessage}")
        }
    }


}