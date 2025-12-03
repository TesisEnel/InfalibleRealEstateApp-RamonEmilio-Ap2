package com.infaliblerealestate.dominio.usecase.carrito

import com.infaliblerealestate.dominio.model.CarritoAddItem
import com.infaliblerealestate.dominio.repository.CarritoRepository
import javax.inject.Inject

class PostCarritoUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(id: String, item: CarritoAddItem) = repository.postCarrito(id, item)
}
