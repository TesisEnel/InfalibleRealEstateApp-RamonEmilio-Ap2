package com.infaliblerealestate.dominio.usecase.propiedades

import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.repository.PropiedadesRepository
import javax.inject.Inject

class SavePropiedadUseCase @Inject constructor(
    private val repository: PropiedadesRepository
) {
    suspend operator fun invoke(id: Int, propiedad: Propiedades): Resource<*> {
        return if(id == 0){
            repository.postPropiedad(propiedad)
        } else {
            repository.putPropiedad(id, propiedad)
        }
    }
}