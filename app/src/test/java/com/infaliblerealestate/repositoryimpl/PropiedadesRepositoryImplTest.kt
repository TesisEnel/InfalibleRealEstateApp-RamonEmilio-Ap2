package com.infaliblerealestate.repositoryimpl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.data.remote.dto.propiedades.response.CategoriaResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.EstadoPropiedadResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesDetalleResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.PropiedadesResponse
import com.infaliblerealestate.data.remote.dto.propiedades.response.UbicacionResponse
import com.infaliblerealestate.data.remote.propiedades.PropiedadesRemoteDataSource
import com.infaliblerealestate.data.repository.PropiedadesRepositoryImpl
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.model.PropiedadesDetalle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PropiedadesRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: PropiedadesRepositoryImpl
    private lateinit var remoteDataSource: PropiedadesRemoteDataSource

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = PropiedadesRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getPropiedades retorna lista exitosamente`() = runTest {
        val propiedadesResponse = listOf(
            createPropiedadesResponse(1),
            createPropiedadesResponse(2)
        )
        coEvery { remoteDataSource.getPropiedades() } returns Resource.Success(propiedadesResponse)

        val result = repository.getPropiedades().first()

        assertEquals(2, result.size)
        coVerify { remoteDataSource.getPropiedades() }
    }

    @Test
    fun `getPropiedades retorna lista vacía cuando hay error`() = runTest {
        coEvery { remoteDataSource.getPropiedades() } returns Resource.Error("Error de red")

        val result = repository.getPropiedades().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getPropiedad retorna propiedad exitosamente`() = runTest {
        val propiedadId = 1
        val propiedadResponse = createPropiedadesResponse(propiedadId)
        coEvery { remoteDataSource.getPropiedad(propiedadId) } returns Resource.Success(propiedadResponse)

        val result = repository.getPropiedad(propiedadId)

        assertTrue(result is Resource.Success)
        assertNotNull(result.data)
        assertEquals(propiedadId, result.data?.propiedadId)
    }

    @Test
    fun `getPropiedad retorna error cuando falla`() = runTest {
        val propiedadId = 1
        coEvery { remoteDataSource.getPropiedad(propiedadId) } returns Resource.Error("Error de red")

        val result = repository.getPropiedad(propiedadId)

        assertTrue(result is Resource.Error)
        assertEquals("Error de red", (result as Resource.Error).message)
    }

    @Test
    fun `putPropiedad actualiza exitosamente`() = runTest {
        val propiedadId = 1
        val propiedad = createPropiedad(propiedadId)
        coEvery { remoteDataSource.putPropiedad(propiedadId, any()) } returns Resource.Success(Unit)

        val result = repository.putPropiedad(propiedadId, propiedad)

        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.putPropiedad(propiedadId, any()) }
    }

    @Test
    fun `putPropiedad retorna error cuando falla`() = runTest {
        val propiedadId = 1
        val propiedad = createPropiedad(propiedadId)
        coEvery { remoteDataSource.putPropiedad(propiedadId, any()) } returns Resource.Error("Error al actualizar")

        val result = repository.putPropiedad(propiedadId, propiedad)

        assertTrue(result is Resource.Error)
        assertEquals("Error al actualizar", (result as Resource.Error).message)
    }

    @Test
    fun `postPropiedad crea propiedad exitosamente`() = runTest {
        val propiedad = createPropiedad(0)
        val response = createPropiedadesResponse(1)
        coEvery { remoteDataSource.postPropiedad(any()) } returns Resource.Success(response)

        val result = repository.postPropiedad(propiedad)

        assertTrue(result is Resource.Success)
        assertEquals(1, result.data?.propiedadId)
        coVerify { remoteDataSource.postPropiedad(any()) }
    }

    @Test
    fun `postPropiedad retorna error cuando falla`() = runTest {
        val propiedad = createPropiedad(0)
        coEvery { remoteDataSource.postPropiedad(any()) } returns Resource.Error("Error al crear")

        val result = repository.postPropiedad(propiedad)

        assertTrue(result is Resource.Error)
        assertEquals("Error al crear", (result as Resource.Error).message)
    }

    @Test
    fun `deletePropiedad elimina exitosamente`() = runTest {
        val propiedadId = 1
        coEvery { remoteDataSource.deletePropiedad(propiedadId) } returns Resource.Success(Unit)

        val result = repository.deletePropiedad(propiedadId)

        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.deletePropiedad(propiedadId) }
    }

    @Test
    fun `deletePropiedad retorna error cuando falla`() = runTest {
        val propiedadId = 1
        coEvery { remoteDataSource.deletePropiedad(propiedadId) } returns Resource.Error("Error al eliminar")

        val result = repository.deletePropiedad(propiedadId)

        assertTrue(result is Resource.Error)
        assertEquals("Error al eliminar", (result as Resource.Error).message)
    }

    @Test
    fun `getCategorias retorna lista exitosamente`() = runTest {
        val categoriasResponse = listOf(
            CategoriaResponse(categoriaId = 1, nombreCategoria = "Residencial", descripcion = ""),
            CategoriaResponse(categoriaId = 2, nombreCategoria = "Comercial", descripcion = "")
        )
        coEvery { remoteDataSource.getCategorias() } returns Resource.Success(categoriasResponse)

        val result = repository.getCategorias().first()

        assertEquals(2, result.size)
        coVerify { remoteDataSource.getCategorias() }
    }

    @Test
    fun `getCategorias retorna lista vacía cuando hay error`() = runTest {
        coEvery { remoteDataSource.getCategorias() } returns Resource.Error("Error de red")

        val result = repository.getCategorias().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getEstadoPropiedades retorna lista exitosamente`() = runTest {
        val estadosResponse = listOf(
            EstadoPropiedadResponse(estadoPropiedadId = 1, nombreEstado = "Disponible", descripcion = ""),
            EstadoPropiedadResponse(estadoPropiedadId = 2, nombreEstado = "Vendida", descripcion = "")
        )
        coEvery { remoteDataSource.getEstadoPropiedades() } returns Resource.Success(estadosResponse)

        val result = repository.getEstadoPropiedades().first()

        assertEquals(2, result.size)
        coVerify { remoteDataSource.getEstadoPropiedades() }
    }

    @Test
    fun `getEstadoPropiedades retorna lista vacía cuando hay error`() = runTest {
        coEvery { remoteDataSource.getEstadoPropiedades() } returns Resource.Error("Error de red")

        val result = repository.getEstadoPropiedades().first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `uploadImages sube imágenes exitosamente`() = runTest {
        val propiedadId = 1
        val images = listOf<MultipartBody.Part>()
        coEvery { remoteDataSource.uploadImages(propiedadId, images) } returns Resource.Success(Unit)

        val result = repository.uploadImages(propiedadId, images)

        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.uploadImages(propiedadId, images) }
    }

    @Test
    fun `uploadImages retorna error cuando falla`() = runTest {
        val propiedadId = 1
        val images = listOf<MultipartBody.Part>()
        coEvery { remoteDataSource.uploadImages(propiedadId, images) } returns Resource.Error("Error al subir")

        val result = repository.uploadImages(propiedadId, images)

        assertTrue(result is Resource.Error)
        assertEquals("Error al subir", (result as Resource.Error).message)
    }

    @Test
    fun `deleteImages elimina imágenes exitosamente`() = runTest {
        val imagenesIds = listOf(1, 2, 3)
        coEvery { remoteDataSource.deleteImages(imagenesIds) } returns Resource.Success(Unit)

        val result = repository.deleteImages(imagenesIds)

        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.deleteImages(imagenesIds) }
    }

    @Test
    fun `deleteImages retorna error cuando falla`() = runTest {
        val imagenesIds = listOf(1, 2, 3)
        coEvery { remoteDataSource.deleteImages(imagenesIds) } returns Resource.Error("Error al eliminar")

        val result = repository.deleteImages(imagenesIds)

        assertTrue(result is Resource.Error)
        assertEquals("Error al eliminar", (result as Resource.Error).message)
    }

    @Test
    fun `getUbicaciones retorna ubicación exitosamente`() = runTest {
        val ubicacionResponse = UbicacionResponse(
            provincias = listOf("provincia", "provincia2"),
            ciudades = listOf("Ciudad1", "Ciudad2")
        )
        coEvery { remoteDataSource.getUbicaciones() } returns Resource.Success(ubicacionResponse)

        val result = repository.getUbicaciones()

        assertTrue(result is Resource.Success)
        assertNotNull(result.data)
        coVerify { remoteDataSource.getUbicaciones() }
    }

    @Test
    fun `getUbicaciones retorna error cuando falla`() = runTest {
        coEvery { remoteDataSource.getUbicaciones() } returns Resource.Error("Error de red")

        val result = repository.getUbicaciones()

        assertTrue(result is Resource.Error)
        assertEquals("Error de red", (result as Resource.Error).message)
    }

    private fun createPropiedadesResponse(id: Int) = PropiedadesResponse(
        propiedadId = id,
        titulo = "Propiedad $id",
        precio = 100000.0,
        moneda = "USD",
        ciudad = "Ciudad",
        estadoProvincia = "Estado",
        tipoTransaccion = "Venta",
        categoriaId = 1,
        fechaPublicacion = "2024-01-01",
        fechaActualizacion = "2024-01-01",
        estadoPropiedadId = 1,
        detalle = PropiedadesDetalleResponse(
            propiedadId = id,
            descripcion = "Descripción",
            habitaciones = 3,
            banos = 2.0,
            parqueo = 1,
            metrosCuadrados = 100.0
        ),
        imagenes = emptyList()
    )

    private fun createPropiedad(id: Int) = Propiedades(
        propiedadId = id,
        administradorId = "admin1",
        titulo = "Propiedad $id",
        precio = 100000.0,
        moneda = "USD",
        ciudad = "Ciudad",
        estadoProvincia = "Estado",
        tipoTransaccion = "Venta",
        categoriaId = 1,
        fechaPublicacion = "2024-01-01",
        fechaActualizacion = "2024-01-01",
        estadoPropiedadId = 1,
        detalle = PropiedadesDetalle(
            propiedadId = id,
            descripcion = "Descripción",
            habitaciones = 3,
            banos = 2.0,
            parqueo = 1,
            metrosCuadrados = 100.0
        ),
        imagenes = emptyList()
    )
}
