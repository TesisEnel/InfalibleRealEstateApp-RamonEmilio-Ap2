package com.infaliblerealestate.dominio.usecase.usuarios

import com.infaliblerealestate.dominio.model.Usuario
import com.infaliblerealestate.dominio.repository.UsuarioRepository
import javax.inject.Inject

class PutUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(id: Int, usuario: Usuario) = repository.putUsuario(id, usuario)
}