package com.infaliblerealestate.dominio.usecase.propiedades

import com.infaliblerealestate.dominio.repository.PropiedadesRepository
import javax.inject.Inject

class GetCategoriasUseCase @Inject constructor(
    private val repository: PropiedadesRepository
) {
    suspend operator fun invoke() = repository.getCategorias()
}