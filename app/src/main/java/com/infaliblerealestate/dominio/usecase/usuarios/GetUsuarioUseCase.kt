package com.infaliblerealestate.dominio.usecase.usuarios

import com.infaliblerealestate.dominio.repository.UsuarioRepository
import javax.inject.Inject

class GetUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(id: String) = repository.getUsuario(id)
}