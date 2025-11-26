package com.infaliblerealestate.dominio.usecase.usuarios

import com.infaliblerealestate.dominio.model.Login
import com.infaliblerealestate.dominio.repository.UsuarioRepository
import javax.inject.Inject

class ValidarCredencialesUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(credenciales: Login) = repository.validarCredenciales(credenciales)
}
