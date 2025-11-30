package com.infaliblerealestate.data.remote.usuarios

import com.infaliblerealestate.data.remote.dto.login.LoginRequest
import com.infaliblerealestate.data.remote.dto.usuarios.UsuarioRequest
import com.infaliblerealestate.data.remote.dto.usuarios.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UsuariosApiService {
    @POST("api/Usuario/validar-credenciales")
    suspend fun validarCredenciales(@Body credenciales: LoginRequest): Response<UsuarioResponse?>

    @GET("api/Usuario/{id}")
    suspend fun getUsuario(@Path("id") id : String): Response<UsuarioResponse?>

    @PUT("api/Usuario/{id}")
    suspend fun putUsuario(@Path("id") id : String, @Body usuario: UsuarioRequest): Response<UsuarioResponse>
}