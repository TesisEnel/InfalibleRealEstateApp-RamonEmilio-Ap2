package com.infaliblerealestate.dominio.repository

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Categorias
import com.infaliblerealestate.dominio.model.EstadoPropiedad
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.model.Ubicacion
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface PropiedadesRepository {
    suspend fun getPropiedades(): Flow<List<Propiedades>>
    suspend fun getPropiedad(id: Int): Resource<Propiedades>
    suspend fun putPropiedad(id: Int, propiedad: Propiedades): Resource<Unit>
    suspend fun postPropiedad(propiedad: Propiedades): Resource<Propiedades>
    suspend fun deletePropiedad(id: Int): Resource<Unit>
    suspend fun getCategorias(): Flow<List<Categorias>>
    suspend fun getEstadoPropiedades(): Flow<List<EstadoPropiedad>>
    suspend fun uploadImages(propiedadId: Int, images: List<MultipartBody.Part>): Resource<Unit>
    suspend fun deleteImages(imagenesIds: List<Int>): Resource<Unit>
    suspend fun getUbicaciones(): Resource<Ubicacion>
}

