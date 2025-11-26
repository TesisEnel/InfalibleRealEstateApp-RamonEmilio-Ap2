package com.infaliblerealestate.dominio.usecase.carrito

import com.infaliblerealestate.dominio.repository.CarritoRepository
import javax.inject.Inject

class GetCarritoByIdUseCase @Inject constructor(
    private val repository: CarritoRepository
) {
    suspend operator fun invoke(id: String) = repository.getCarritoByUserid(id)
}