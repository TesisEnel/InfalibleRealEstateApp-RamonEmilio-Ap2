package com.infaliblerealestate.dominio.usecase.propiedades

import com.infaliblerealestate.dominio.repository.PropiedadesRepository
import jakarta.inject.Inject

class DeleteImagesUseCase @Inject constructor(
    private val repository: PropiedadesRepository
) {
    suspend operator fun invoke(imagenesIds: List<Int>) = repository.deleteImages(imagenesIds)

}