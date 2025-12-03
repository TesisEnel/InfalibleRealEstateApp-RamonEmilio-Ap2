package com.infaliblerealestate.repositoryimpl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.data.remote.carrito.CarritoRemoteDataSource
import com.infaliblerealestate.data.remote.dto.carrito.CarritoResponse
import com.infaliblerealestate.data.repository.CarritoRepositoryImpl
import com.infaliblerealestate.dominio.model.CarritoAddItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CarritoRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: CarritoRepositoryImpl
    private lateinit var remoteDataSource: CarritoRemoteDataSource

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = CarritoRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getCarritoByUserid retorna carrito exitosamente`() = runTest {
        val userId = "user123"
        val carritoResponse = CarritoResponse(
            carritoId = 1,
            usuarioId = userId,
            items = listOf()
        )
        coEvery { remoteDataSource.getCarritoByUserid(userId) } returns Resource.Success(carritoResponse)

        val result = repository.getCarritoByUserid(userId)

        assertTrue(result is Resource.Success)
        assertNotNull(result.data)
        coVerify { remoteDataSource.getCarritoByUserid(userId) }
    }

    @Test
    fun `getCarritoByUserid retorna error cuando falla la petición`() = runTest {
        val userId = "user123"
        coEvery { remoteDataSource.getCarritoByUserid(userId) } returns Resource.Error("Error de red")

        val result = repository.getCarritoByUserid(userId)

        assertTrue(result is Resource.Error)
        assertEquals("Error de red", (result as Resource.Error).message)
    }

    @Test
    fun `postCarrito agrega item exitosamente`() = runTest {
        val userId = "user123"
        val item = CarritoAddItem(propiedadId = 1)
        coEvery { remoteDataSource.postCarrito(userId, any()) } returns Resource.Success(Unit)

        val result = repository.postCarrito(userId, item)

        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.postCarrito(userId, any()) }
    }

    @Test
    fun `postCarrito retorna error cuando falla`() = runTest {
        val userId = "user123"
        val item = CarritoAddItem(propiedadId = 1)
        coEvery { remoteDataSource.postCarrito(userId, any()) } returns Resource.Error("Error al agregar")

        val result = repository.postCarrito(userId, item)

        assertTrue(result is Resource.Error)
        assertEquals("Error al agregar", (result as Resource.Error).message)
    }

    @Test
    fun `deletePropiedadDeCarrito elimina exitosamente`() = runTest {
        val userId = "user123"
        val propiedadId = 1
        coEvery { remoteDataSource.deletePropiedadDeCarrito(userId, propiedadId) } returns Resource.Success(Unit)

        val result = repository.deletePropiedadDeCarrito(userId, propiedadId)

        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.deletePropiedadDeCarrito(userId, propiedadId) }
    }

    @Test
    fun `deletePropiedadDeCarrito retorna error cuando falla`() = runTest {
        val userId = "user123"
        val propiedadId = 1
        coEvery { remoteDataSource.deletePropiedadDeCarrito(userId, propiedadId) } returns Resource.Error("Error al eliminar")

        val result = repository.deletePropiedadDeCarrito(userId, propiedadId)

        assertTrue(result is Resource.Error)
        assertEquals("Error al eliminar", (result as Resource.Error).message)
    }
}
