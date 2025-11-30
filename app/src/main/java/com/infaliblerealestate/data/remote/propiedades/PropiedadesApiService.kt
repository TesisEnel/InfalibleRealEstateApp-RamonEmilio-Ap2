package com.infaliblerealestate.data.remote.propiedades

import com.infaliblerealestate.data.remote.dto.propiedades.request.PropiedadesRequest
import com.infaliblerealestate.data.remote.dto.propiedades.response.CategoriaResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.EstadoPropiedadResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PropiedadesApiService {
    @GET("api/Propiedades")
    suspend fun getPropiedades(): Response<List<PropiedadesResponse>>
    @GET("api/Propiedades/{id}")
    suspend fun getPropiedad(@Path("id") id : Int): Response<PropiedadesResponse>
    @PUT("api/Propiedades/{id}")
    suspend fun putPropiedad(@Path("id")id : Int, @Body propiedad: PropiedadesRequest): Response<Unit>
    @POST("api/Propiedades")
    suspend fun postPropiedad(@Body propiedad: PropiedadesRequest): Response<PropiedadesResponse>
    @DELETE("api/Propiedades/{id}")
    suspend fun deletePropiedad(@Path("id")id : Int): Response<Unit>
    @GET("api/Propiedades/categorias")
    suspend fun getCategorias(): Response<List<CategoriaResponse>>
    @GET("api/propiedades/estado-propiedades")
    suspend fun getEstadoPropiedades(): Response<List<EstadoPropiedadResponse>>

}