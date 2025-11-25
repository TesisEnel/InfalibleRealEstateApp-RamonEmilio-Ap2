package com.infaliblerealestate.data.remote.Propiedades

import com.infaliblerealestate.data.remote.dto.propiedades.request.PropiedadesRequest
import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PropiedadesApiService {
    @GET("api/Propiedades")
    suspend fun getPropiedades(): Response<List<PropiedadesResponse>>
    @GET("api/Propiedades/{id}")
    suspend fun getPropiedad(@Path("id") id : Int): Response<PropiedadesResponse>
    @PUT("api/Propiedades/{id}")
    suspend fun putPropiedad(@Path("id")id : Int, @Body propiedad: PropiedadesRequest): Response<Unit>
}