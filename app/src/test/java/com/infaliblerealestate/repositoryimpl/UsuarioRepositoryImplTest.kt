package com.infaliblerealestate.repositoryimpl

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.infaliblerealestate.data.local.datasource.UsuarioLocalDataSource
import com.infaliblerealestate.data.local.entities.UsuarioEntity
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.data.remote.dto.usuarios.UsuarioResponse
import com.infaliblerealestate.data.remote.usuarios.UsuarioRemoteDataSource
import com.infaliblerealestate.data.repository.UsuarioRepositoryImpl
import com.infaliblerealestate.dominio.model.Login
import com.infaliblerealestate.dominio.model.Usuario
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UsuarioRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: UsuarioRepositoryImpl
    private lateinit var remoteDataSource: UsuarioRemoteDataSource
    private lateinit var localDataSource: UsuarioLocalDataSource

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        localDataSource = mockk(relaxed = true)
        repository = UsuarioRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `validarCredenciales retorna usuario exitosamente`() = runTest {
        val login = Login(email = "danielvi12@gmail.com", password = "pass123")
        val usuarioResponse = createUsuarioResponse("1")
        coEvery { remoteDataSource.validarCredenciales(any()) } returns Resource.Success(usuarioResponse)

        val result = repository.validarCredenciales(login)

        assertTrue(result is Resource.Success)
        assertNotNull(result.data)
        assertEquals("danielvi12@gmail.com", result.data?.email)
    }

    @Test
    fun `validarCredenciales retorna error cuando falla autenticación`() = runTest {
        val login = Login(email = "danielvi12@gmail.com", password = "wrong")
        coEvery { remoteDataSource.validarCredenciales(any()) } returns Resource.Error("Credenciales inválidas")

        val result = repository.validarCredenciales(login)

        assertTrue(result is Resource.Error)
        assertEquals("Credenciales inválidas", (result as Resource.Error).message)
    }

    @Test
    fun `getUsuario retorna usuario exitosamente`() = runTest {
        val userId = "user123"
        val usuarioResponse = createUsuarioResponse(userId)
        coEvery { remoteDataSource.getUsuario(userId) } returns Resource.Success(usuarioResponse)

        val result = repository.getUsuario(userId)

        assertTrue(result is Resource.Success)
        assertNotNull(result.data)
        assertEquals(userId, result.data?.id)
    }

    @Test
    fun `getUsuario retorna error cuando falla`() = runTest {
        val userId = "user123"
        coEvery { remoteDataSource.getUsuario(userId) } returns Resource.Error("Error de red")

        val result = repository.getUsuario(userId)

        assertTrue(result is Resource.Error)
        assertEquals("Error de red", (result as Resource.Error).message)
    }

    @Test
    fun `putUsuario actualiza exitosamente`() = runTest {
        val userId = "user123"
        val usuario = createUsuario(userId)
        val response = createUsuarioResponse(userId)
        coEvery { remoteDataSource.putUsuario(userId, any()) } returns Resource.Success(response)

        val result = repository.putUsuario(userId, usuario)

        assertTrue(result is Resource.Success)
        assertEquals(userId, result.data?.id)
        coVerify { remoteDataSource.putUsuario(userId, any()) }
    }

    @Test
    fun `putUsuario retorna error cuando falla`() = runTest {
        val userId = "user123"
        val usuario = createUsuario(userId)
        coEvery { remoteDataSource.putUsuario(userId, any()) } returns Resource.Error("Error al actualizar")

        val result = repository.putUsuario(userId, usuario)

        assertTrue(result is Resource.Error)
        assertEquals("Error al actualizar", (result as Resource.Error).message)
    }

    @Test
    fun `getUsuarioActual retorna flow de usuario local`() = runTest {
        val usuarioEntity = createUsuarioEntity("user123")
        coEvery { localDataSource.getUsuarioActual() } returns flowOf(usuarioEntity)

        val result = repository.getUsuarioActual().first()

        assertNotNull(result)
        assertEquals("user123", result?.id)
    }

    @Test
    fun `getUsuarioActual retorna null cuando no hay usuario`() = runTest {
        coEvery { localDataSource.getUsuarioActual() } returns flowOf(null)

        val result = repository.getUsuarioActual().first()

        assertEquals(null, result)
    }

    @Test
    fun `insertUsuario guarda en base de datos local`() = runTest {
        val usuario = createUsuario("user123")
        coEvery { localDataSource.insertUsuario(any()) } just Runs

        repository.insertUsuario(usuario)

        coVerify { localDataSource.insertUsuario(any()) }
    }

    @Test
    fun `updateUsuarioLocal actualiza exitosamente`() = runTest {
        val usuario = createUsuario("user123")
        coEvery { localDataSource.updateUsuario(any()) } just Runs

        val result = repository.updateUsuarioLocal(usuario)

        assertTrue(result is Resource.Success)
        coVerify { localDataSource.updateUsuario(any()) }
    }

    @Test
    fun `updateUsuarioLocal retorna error cuando falla`() = runTest {
        val usuario = createUsuario("user123")
        coEvery { localDataSource.updateUsuario(any()) } throws Exception("Error de base de datos")

        val result = repository.updateUsuarioLocal(usuario)

        assertTrue(result is Resource.Error)
        assertEquals("Error de base de datos", (result as Resource.Error).message)
    }

    @Test
    fun `syncUsuarioToRemote sincroniza exitosamente`() = runTest {
        val userId = "user123"
        val usuarioEntity = createUsuarioEntity(userId)
        val response = createUsuarioResponse(userId)

        coEvery { localDataSource.getUsuarioById(userId) } returns usuarioEntity
        coEvery { remoteDataSource.putUsuario(userId, any()) } returns Resource.Success(response)
        coEvery { localDataSource.updateUsuario(any()) } just Runs

        val result = repository.syncUsuarioToRemote(userId)

        assertTrue(result is Resource.Success)
        coVerify { remoteDataSource.putUsuario(userId, any()) }
        coVerify { localDataSource.updateUsuario(any()) }
    }

    @Test
    fun `syncUsuarioToRemote retorna error cuando usuario no existe localmente`() = runTest {
        val userId = "user123"
        coEvery { localDataSource.getUsuarioById(userId) } returns null

        val result = repository.syncUsuarioToRemote(userId)

        assertTrue(result is Resource.Error)
        assertEquals("Usuario no encontrado en base de datos local", (result as Resource.Error).message)
        coVerify(exactly = 0) { remoteDataSource.putUsuario(any(), any()) }
    }

    @Test
    fun `syncUsuarioToRemote retorna error cuando falla la sincronización remota`() = runTest {
        val userId = "user123"
        val usuarioEntity = createUsuarioEntity(userId)

        coEvery { localDataSource.getUsuarioById(userId) } returns usuarioEntity
        coEvery { remoteDataSource.putUsuario(userId, any()) } returns Resource.Error("Error de red")

        val result = repository.syncUsuarioToRemote(userId)

        assertTrue(result is Resource.Error)
        assertEquals("Error de red", (result as Resource.Error).message)
        coVerify(exactly = 0) { localDataSource.updateUsuario(any()) }
    }

    @Test
    fun `logout limpia sesión local`() = runTest {
        coEvery { localDataSource.clearSession() } just Runs

        repository.logout()

        coVerify { localDataSource.clearSession() }
    }

    private fun createUsuarioResponse(id: String) = UsuarioResponse(
        id = id,
        userName = "danielvi12",
        email = "danielvi12@gmail.com",
        phoneNumber = "1234567890",
        nombre = "Daniel",
        apellido = "Test",
        estadoUsuario = "Activo",
        rol = "Usuario"
    )

    private fun createUsuario(id: String) = Usuario(
        id = id,
        userName = "danielvi12",
        email = "danielvi12@gmail.com",
        phoneNumber = "1234567890",
        nombre = "Daniel",
        apellido = "Test",
        estadoUsuario = "Activo",
        rol = "Usuario"
    )

    private fun createUsuarioEntity(id: String) = UsuarioEntity(
        id = id,
        userName = "danielvi12",
        email = "danielvi12@gmail.com",
        phoneNumber = "1234567890",
        nombre = "Daniel",
        apellido = "Test",
        estadoUsuario = "Activo",
        rol = "Usuario",
        lastSync = 0L
    )
}
