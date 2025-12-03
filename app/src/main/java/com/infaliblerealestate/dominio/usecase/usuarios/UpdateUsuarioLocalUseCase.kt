package com.infaliblerealestate.dominio.usecase.usuarios

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Usuario
import com.infaliblerealestate.dominio.repository.UsuarioRepository
import javax.inject.Inject

class UpdateUsuarioLocalUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(usuario: Usuario): Resource<Usuario> {
        return repository.updateUsuarioLocal(usuario)
    }
}