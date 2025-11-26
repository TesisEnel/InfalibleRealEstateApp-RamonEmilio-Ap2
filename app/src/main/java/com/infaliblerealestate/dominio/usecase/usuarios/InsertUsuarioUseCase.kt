package com.infaliblerealestate.dominio.usecase.usuarios

import com.infaliblerealestate.dominio.model.Usuario
import com.infaliblerealestate.dominio.repository.UsuarioRepository
import jakarta.inject.Inject

class InsertUsuarioUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(usuario: Usuario) = repository.insertUsuario(usuario)
}