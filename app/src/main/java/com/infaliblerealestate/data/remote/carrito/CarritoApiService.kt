package com.infaliblerealestate.data.remote.carrito

import com.infaliblerealestate.data.remote.dto.carrito.CarritoAddItemRequest
import com.infaliblerealestate.data.remote.dto.carrito.CarritoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CarritoApiService {
    @GET("api/Carrito/{id}")
    suspend fun getCarritoByUserId(@Path("id") id: String): Response<CarritoResponse>

    @POST("api/Carrito/{id}")
    suspend fun postCarrito(@Path("id") id: String, @Body item: CarritoAddItemRequest): Response<Unit>
}