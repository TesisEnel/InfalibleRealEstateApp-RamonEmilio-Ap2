package com.infaliblerealestate.data.remote.usuarios

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.data.remote.dto.login.LoginRequest
import com.infaliblerealestate.data.remote.dto.usuarios.UsuarioRequest
import com.infaliblerealestate.data.remote.dto.usuarios.UsuarioResponse
import javax.inject.Inject

class UsuarioRemoteDataSource @Inject constructor(
    private val apiService: UsuariosApiService
) {
    suspend fun validarCredenciales(credenciales: LoginRequest): Resource<UsuarioResponse?>{
        return try {
            val response = apiService.validarCredenciales(credenciales)
            if (response.isSuccessful){
                response.body().let{Resource.Success(it)}
            }else{
                return Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (e: Exception){
            Resource.Error("Error: ${e.localizedMessage} ?: Ocurrio un error al validar las credenciales")
        }
    }

    suspend fun getUsuario(id: String): Resource<UsuarioResponse?>{
        return try {
            val response = apiService.getUsuario(id)
            if(response.isSuccessful){
                response.body().let{Resource.Success(it)}

            }else{
                return Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (e: Exception){
            Resource.Error("Error ${e.localizedMessage} ?: Ocurrio un error al obtener el usuario")
        }
    }

    suspend fun putUsuario(id: String, usuario: UsuarioRequest): Resource<UsuarioResponse?>{
        return try{
            val response = apiService.putUsuario(id, usuario)
            if(response.isSuccessful){
                response.body().let{Resource.Success(it)}
            }else{
                return Resource.Error("Error ${response.code()}: ${response.message()}")
            }
        }catch (e: Exception){
            Resource.Error("Error ${e.localizedMessage} ?: Ocurrio un error al actualizar el usuario")
        }

    }

}