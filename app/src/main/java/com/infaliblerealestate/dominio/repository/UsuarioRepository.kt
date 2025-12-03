package com.infaliblerealestate.dominio.repository

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Login
import com.infaliblerealestate.dominio.model.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    suspend fun validarCredenciales(credenciales: Login): Resource<Usuario?>
    suspend fun getUsuario(id: String): Resource<Usuario?>
    suspend fun putUsuario(id: String, usuario: Usuario): Resource<Usuario?>
    fun getUsuarioActual(): Flow<Usuario?>
    suspend fun logout()
    suspend fun insertUsuario(usuario: Usuario)
    suspend fun updateUsuarioLocal(usuario: Usuario): Resource<Usuario>
    suspend fun syncUsuarioToRemote(id: String): Resource<Usuario>



}