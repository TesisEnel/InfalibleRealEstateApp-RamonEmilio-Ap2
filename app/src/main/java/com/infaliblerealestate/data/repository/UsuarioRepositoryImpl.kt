package com.infaliblerealestate.data.repository

import com.infaliblerealestate.data.local.datasource.UsuarioLocalDataSource
import com.infaliblerealestate.data.mapper.toDomain
import com.infaliblerealestate.data.mapper.toDto
import com.infaliblerealestate.data.mapper.toEntity
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.data.remote.usuarios.UsuarioRemoteDataSource
import com.infaliblerealestate.dominio.model.Login
import com.infaliblerealestate.dominio.model.Usuario
import com.infaliblerealestate.dominio.repository.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UsuarioRepositoryImpl @Inject constructor(
    private val remoteDataSource: UsuarioRemoteDataSource,
    private val localDataSource: UsuarioLocalDataSource
): UsuarioRepository {

    companion object {
        private const val SYNC_INTERVAL = 24 * 60 * 60 * 1000L
    }

    override suspend fun validarCredenciales(credenciales: Login): Resource<Usuario?> {
        when(val result = remoteDataSource.validarCredenciales(credenciales.toDto())){
            is Resource.Success -> {
                val usuario = result.data
                return Resource.Success(usuario?.toDomain())
            }
            is Resource.Error -> {
                return Resource.Error(result.message ?: "Error")
            }
            else -> {
                return Resource.Error("Error al validar las credenciales")
            }
        }
    }

    override suspend fun getUsuario(id: String): Resource<Usuario?> {
        return when(val result = remoteDataSource.getUsuario(id)){
            is Resource.Success -> {
                val usuario = result.data
                Resource.Success(usuario?.toDomain())
            }
            is Resource.Error -> {
                Resource.Error(result.message ?: "Error")
            }
            else -> Resource.Error("Error obtener el usuario")
        }
    }

    override suspend fun putUsuario(id: String, usuario: Usuario): Resource<Usuario?> {
        when(val result = remoteDataSource.putUsuario(id, usuario.toDto())){
            is Resource.Success -> {
                val usuario = result.data
                return Resource.Success(usuario?.toDomain())
            }
            is Resource.Error -> {
                return Resource.Error(result.message ?: "Error")
            }
            else -> {
                return Resource.Error("Error al actualizar el usuario")
            }
        }

    }

    override fun getUsuarioActual(): Flow<Usuario?> {
        return localDataSource.getUsuarioActual().map { it?.toDomain() }
    }

    override suspend fun insertUsuario(usuario: Usuario) {
        localDataSource.insertUsuario(usuario.toEntity())
    }

    override suspend fun syncUsuario(id: String): Resource<Usuario?> {
        val lastSync = localDataSource.getLastSync(id) ?: 0
        val now = System.currentTimeMillis()

        if (now - lastSync < SYNC_INTERVAL) {
            return Resource.Success(null)
        }

        val result = remoteDataSource.getUsuario(id)
        return when(result) {
            is Resource.Success -> {
                val usuario = result.data?.toDomain()
                usuario?.let {
                    localDataSource.insertUsuario(it.toEntity())
                }
                Resource.Success(usuario)
            }
            is Resource.Error -> Resource.Error(result.message ?: "Error")
            else -> Resource.Error("Error al sincronizar usuario")
        }
    }

    override suspend fun logout() {
        localDataSource.clearSession()
    }


}