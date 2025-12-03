package com.infaliblerealestate.dominio.usecase.usuarios

import com.infaliblerealestate.dominio.repository.UsuarioRepository
import javax.inject.Inject

class GetUsuarioActualUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    operator fun invoke() = repository.getUsuarioActual()
}
