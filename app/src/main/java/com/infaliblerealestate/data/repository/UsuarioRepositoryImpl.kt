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

    override suspend fun updateUsuarioLocal(usuario: Usuario): Resource<Usuario> {
        return try {
            val entity = usuario.toEntity()
            localDataSource.updateUsuario(entity)
            Resource.Success(usuario)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error al actualizar usuario local")
        }
    }

    override suspend fun syncUsuarioToRemote(id: String): Resource<Usuario> {
        return try {
            val usuarioEntity = localDataSource.getUsuarioById(id)
                ?: return Resource.Error("Usuario no encontrado en base de datos local")

            val usuario = usuarioEntity.toDomain()

            when(val result = remoteDataSource.putUsuario(id, usuario.toDto())) {
                is Resource.Success -> {
                    val usuarioActualizado = result.data?.toDomain()
                        ?: return Resource.Error("Respuesta vacía del servidor")

                    localDataSource.updateUsuario(usuarioActualizado.toEntity())
                    Resource.Success(usuarioActualizado)
                }
                is Resource.Error -> {
                    Resource.Error(result.message ?: "Error al sincronizar con servidor")
                }
                else -> {
                    Resource.Error("Error al actualizar el usuario en el servidor")
                }
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de red al sincronizar")
        }
    }

    override suspend fun logout() {
        localDataSource.clearSession()
    }


}