package com.infaliblerealestate.data.local.datasource

import com.infaliblerealestate.data.local.dao.UsuarioDao
import com.infaliblerealestate.data.local.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsuarioLocalDataSource @Inject constructor(
    private val dao: UsuarioDao
) {
    fun getUsuario(id: String): Flow<UsuarioEntity?> = dao.getUsuario(id)

    fun getUsuarioActual(): Flow<UsuarioEntity?> = dao.getUsuarioActual()

    suspend fun insertUsuario(usuario: UsuarioEntity) = dao.insertUsuario(usuario)

    suspend fun updateUsuario(usuario: UsuarioEntity) = dao.updateUsuario(usuario)

    suspend fun getUsuarioById(id: String): UsuarioEntity? = dao.getUsuarioById(id)

    suspend fun clearSession() = dao.deleteAllUsuarios()

    suspend fun getLastSync(id: String): Long? = dao.getLastSync(id)

}