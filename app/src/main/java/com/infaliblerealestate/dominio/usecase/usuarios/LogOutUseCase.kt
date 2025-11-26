package com.infaliblerealestate.dominio.usecase.usuarios

import com.infaliblerealestate.dominio.repository.UsuarioRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) {
    suspend operator fun invoke() {
        usuarioRepository.logout()
    }
}