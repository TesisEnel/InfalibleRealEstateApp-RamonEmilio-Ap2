package com.infaliblerealestate.dominio.usecase.propiedades

import com.infaliblerealestate.dominio.repository.PropiedadesRepository
import javax.inject.Inject

class DeletePropiedadUseCase @Inject constructor(
    private val  repository: PropiedadesRepository
)  {
    suspend operator fun invoke(id: Int) = repository.deletePropiedad(id)
}