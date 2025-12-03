package com.infaliblerealestate.dominio.usecase.carrito

import com.infaliblerealestate.dominio.repository.CarritoRepository
import jakarta.inject.Inject

class DeletePropiedadDeCarrito @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(id: String, propiedadId: Int) = repository.deletePropiedadDeCarrito(id, propiedadId)
}