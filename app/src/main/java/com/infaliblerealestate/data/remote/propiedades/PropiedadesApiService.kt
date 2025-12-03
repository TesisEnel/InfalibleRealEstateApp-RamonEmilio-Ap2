package com.infaliblerealestate.data.remote.propiedades

import com.infaliblerealestate.data.remote.dto.propiedades.request.PropiedadesRequest
import com.infaliblerealestate.data.remote.dto.propiedades.response.CategoriaResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.EstadoPropiedadResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.UbicacionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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
    @Multipart
    @POST("api/Imagenes/upload/{propiedadId}")
    suspend fun uploadImages(@Path("propiedadId") propiedadId: Int, @Part files: List<MultipartBody.Part>): Response<Unit>
    @DELETE("api/Imagenes/Delete")
    suspend fun deleteImages(@Query("imagenesIds") imagenesIds: List<Int>): Response<Unit>
    @GET("api/Propiedades/ubicaciones")
    suspend fun getUbicaciones(): Response<UbicacionResponse>
}
