package com.infaliblerealestate.presentation.upsertPropiedad

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infaliblerealestate.data.remote.Resource
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.model.PropiedadesDetalle
import com.infaliblerealestate.dominio.usecase.propiedades.DeleteImagesUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetCategoriasUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetEstadoPropiedadUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.DeletePropiedadUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.GetPropiedadUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.SavePropiedadUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.UploadImagesUseCase
import com.infaliblerealestate.dominio.usecase.propiedades.getUbicacionesUseCase
import com.infaliblerealestate.dominio.usecase.usuarios.GetUsuarioActualUseCase
import com.infaliblerealestate.presentation.util.validation.PropiedadesValidation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class UpsertPropiedadViewModel @Inject constructor(
    private val getCategoriasUseCase: GetCategoriasUseCase,
    private val getEstadoPropiedadUseCase: GetEstadoPropiedadUseCase,
    private val deletePropiedadUseCase: DeletePropiedadUseCase,
    private val savePropiedadUseCase: SavePropiedadUseCase,
    private val getPropiedadUseCase: GetPropiedadUseCase,
    private val upLoadImagesUseCase: UploadImagesUseCase,
    private val getUsuarioActualUseCase: GetUsuarioActualUseCase,
    private val deleteImagesUseCase: DeleteImagesUseCase,
    private val getUbicacionesUseCase: getUbicacionesUseCase,
    private val validation: PropiedadesValidation
): ViewModel() {

    private val _state = MutableStateFlow(UpsertPropiedadUiState())
    val state: StateFlow<UpsertPropiedadUiState> = _state.asStateFlow()

    init {
        loadListas()
        getUsuarioActual()
    }

    fun onEvent(event: UpsertPropiedadUiEvent){
        when(event){
            is UpsertPropiedadUiEvent.LoadPropiedad -> {loadPropiedad(event.id)}
            is UpsertPropiedadUiEvent.SavePropiedad -> {savePropiedad()}
            is UpsertPropiedadUiEvent.ClearForm -> {clearForm()}
            is UpsertPropiedadUiEvent.OnTituloChanged -> {_state.update { it.copy(titulo = event.titulo) }}
            is UpsertPropiedadUiEvent.OnPrecioChanged -> {_state.update { it.copy(precio = event.precio) }}
            is UpsertPropiedadUiEvent.OnMonedaChanged -> {_state.update { it.copy(moneda = event.moneda) }}
            is UpsertPropiedadUiEvent.OnCiudadChanged -> {_state.update {
                it.copy(
                    ciudad = event.ciudad,
                    filteredCiudades = it.ciudades.filter { c ->
                        c.contains(event.ciudad, ignoreCase = true)
                    }
                )
            }}
            is UpsertPropiedadUiEvent.OnEstadoProvinciachanged -> {_state.update {
                it.copy(
                    estadoProvincia = event.estadoPropiedadProvincia,
                    filteredProvincias = it.provincias.filter { p ->
                        p.contains(event.estadoPropiedadProvincia, ignoreCase = true)
                    }
                )
            }}
            is UpsertPropiedadUiEvent.OnTipoTransaccionChanged -> {_state.update { it.copy(tipoTransaccion = event.tipoTransaccion) }}
            is UpsertPropiedadUiEvent.OnCategoriaIdChanged -> {_state.update { it.copy(categoriaId = event.categoriaId, categoria = event.categoria) }}
            is UpsertPropiedadUiEvent.OnEstadoPropiedadIdChanged -> {_state.update { it.copy(estadoPropiedadId = event.estadoPropiedadId, estadoPropiedad = event.estado) }}
            is UpsertPropiedadUiEvent.OnDescripcionChanged -> {_state.update { it.copy(descripcion = event.descripcion) }}
            is UpsertPropiedadUiEvent.OnHabitacionesChanged -> {_state.update { it.copy(habitaciones = event.habitaciones) }}
            is UpsertPropiedadUiEvent.OnBanosChanged -> {_state.update { it.copy(banos = event.banos) }}
            is UpsertPropiedadUiEvent.OnParqueoChanged -> {_state.update { it.copy(parqueo = event.parqueo) }}
            is UpsertPropiedadUiEvent.OnMetrosCuadradosChanged -> {_state.update { it.copy(metrosCuadrados = event.metrosCuadrados) }}
            is UpsertPropiedadUiEvent.OnImagesSelected -> { seleccionarImagen(event.uris) }
            is UpsertPropiedadUiEvent.OnDeleteSelectedImage -> {deleteSelectedImage(event.uri)}
            is UpsertPropiedadUiEvent.OnDeletePropiedad -> {deletePropiedad(event.propiedadId)}
            is UpsertPropiedadUiEvent.ShowDeleteDialog -> {_state.update { it.copy(showDeleteDialog = true) }}
            is UpsertPropiedadUiEvent.HideDeleteDialog -> {_state.update { it.copy(showDeleteDialog = false) }}
            is UpsertPropiedadUiEvent.OnDeleteServerImage -> {updateDeleteListImages(event.imagenId)}
        }
    }

    fun getUsuarioActual() {
        viewModelScope.launch {
            getUsuarioActualUseCase().collect { usuario ->
                _state.update { state -> state.copy(usuarioId = usuario?.id) }
                val esAdmin = usuario?.rol?.equals("Admin", ignoreCase = true) == true
                if (!esAdmin) {
                    _state.update { it.copy(shouldNavigateBack = true) }
                }
            }
        }
    }

    fun loadListas(){
        viewModelScope.launch {
            getCategoriasUseCase().collect { categorias ->
                _state.update { it.copy(categorias = categorias) }
            }
            getEstadoPropiedadUseCase().collect { estados ->
                _state.update { it.copy(estadosPropiedades = estados) }
            }
            when(val ubicaciones = getUbicacionesUseCase()){
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            provincias = ubicaciones.data?.provincias ?: emptyList(),
                            ciudades = ubicaciones.data?.ciudades ?: emptyList()
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { it.copy(userMessage = "Error al cargar las ubicaciones") }
                }
                else -> {
                    _state.update { it.copy(userMessage = "Error desconocido al cargar la lista de ubicaciones") }
                }
            }

        }
    }

    fun loadPropiedad(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val stateConListas = withTimeoutOrNull(4000) {
                state.filter { it.categorias.isNotEmpty() && it.estadosPropiedades.isNotEmpty() }
                    .first()
            }

            val listasRef = stateConListas ?: _state.value

            when (val propiedad = getPropiedadUseCase(id)) {
                is Resource.Success -> {
                    propiedad.data?.let { p ->

                        val estadoNombre = listasRef.estadosPropiedades
                            .find { it.estadoPropiedadId == p.estadoPropiedadId }
                            ?.nombreEstado ?: ""

                        val categoriaNombre = listasRef.categorias
                            .find { it.categoriaId == p.categoriaId }
                            ?.nombreCategoria ?: ""

                        _state.update {
                            it.copy(
                                propiedadAEditar = p,
                                isEditing = true,
                                titulo = p.titulo,
                                precio = p.precio,
                                moneda = p.moneda,
                                ciudad = p.ciudad,
                                estadoProvincia = p.estadoProvincia,
                                tipoTransaccion = p.tipoTransaccion,
                                categoriaId = p.categoriaId,
                                categoria = categoriaNombre,
                                estadoPropiedadId = p.estadoPropiedadId,
                                estadoPropiedad = estadoNombre,
                                fechaPublicacion = p.fechaPublicacion,
                                fechaActualizacion = p.fechaActualizacion,
                                descripcion = p.detalle.descripcion,
                                habitaciones = p.detalle.habitaciones,
                                banos = p.detalle.banos,
                                parqueo = p.detalle.parqueo,
                                metrosCuadrados = p.detalle.metrosCuadrados,
                                imagenesCargadas = p.imagenes,
                                selectedImages = emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(isLoading = false, userMessage = "Error al cargar la propiedad")
                    }
                }

                else -> {
                    _state.update {
                        it.copy(isLoading = false, userMessage = "Error desconocido")
                    }
                }
            }
        }
    }


    fun savePropiedad() {
        viewModelScope.launch {
            if (!validarDatos()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        userMessage = "Por favor, corrija los errores en el formulario"
                    )
                }
                return@launch
            }
            _state.update { it.copy(isLoading = true) }
            val fecha = java.time.Instant.now().toString()

            if (_state.value.isEditing) {
                val propiedadAEditar = _state.value.propiedadAEditar!!
                val propiedadActualizada = Propiedades(
                    propiedadId = propiedadAEditar.propiedadId,
                    administradorId = _state.value.usuarioId,
                    titulo = _state.value.titulo,
                    precio = _state.value.precio,
                    moneda = _state.value.moneda,
                    ciudad = _state.value.ciudad,
                    estadoProvincia = _state.value.estadoProvincia,
                    tipoTransaccion = _state.value.tipoTransaccion,
                    categoriaId = _state.value.categoriaId,
                    estadoPropiedadId = _state.value.estadoPropiedadId,
                    fechaPublicacion = propiedadAEditar.fechaPublicacion,
                    fechaActualizacion = fecha,
                    detalle = PropiedadesDetalle(
                        propiedadId = propiedadAEditar.propiedadId,
                        descripcion = _state.value.descripcion,
                        habitaciones = _state.value.habitaciones,
                        banos = _state.value.banos,
                        parqueo = _state.value.parqueo,
                        metrosCuadrados = _state.value.metrosCuadrados
                    ),
                    imagenes = emptyList()
                )

                when (val result = savePropiedadUseCase(propiedadActualizada.propiedadId, propiedadActualizada)) {
                    is Resource.Success -> {
                        handleImageUpdates(propiedadActualizada.propiedadId)
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(isLoading = false, userMessage = result.message)
                        }
                    }
                    else -> {
                        _state.update {
                            it.copy(isLoading = false, userMessage = "Error al guardar la propiedad")
                        }
                    }
                }
            } else {
                val nuevaPropiedad = Propiedades(
                    propiedadId = 0,
                    titulo = _state.value.titulo,
                    precio = _state.value.precio,
                    moneda = _state.value.moneda,
                    ciudad = _state.value.ciudad,
                    estadoProvincia = _state.value.estadoProvincia,
                    tipoTransaccion = _state.value.tipoTransaccion,
                    categoriaId = _state.value.categoriaId,
                    administradorId = _state.value.usuarioId,
                    estadoPropiedadId = _state.value.estadoPropiedadId,
                    fechaPublicacion = fecha,
                    fechaActualizacion = fecha,
                    detalle = PropiedadesDetalle(
                        propiedadId = 0,
                        descripcion = _state.value.descripcion,
                        habitaciones = _state.value.habitaciones,
                        banos = _state.value.banos,
                        parqueo = _state.value.parqueo,
                        metrosCuadrados = _state.value.metrosCuadrados
                    ),
                    imagenes = emptyList()
                )

                when (val result = savePropiedadUseCase(0, nuevaPropiedad)) {
                    is Resource.Success -> {
                        val newPropiedad = result.data as? Propiedades
                        if (newPropiedad?.propiedadId != null) {
                            uploadImages(newPropiedad.propiedadId)
                        } else {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    userMessage = "Propiedad guardada, pero no se pudo guardar imágenes.",
                                )
                            }
                            clearForm()
                        }
                    }
                    is Resource.Error -> {
                        _state.update {
                            it.copy(isLoading = false, userMessage = result.message)
                        }
                    }
                    else -> {
                        _state.update {
                            it.copy(isLoading = false, userMessage = "Error al guardar la propiedad")
                        }
                    }
                }
            }
        }
    }

    fun validarDatos(): Boolean {
            val tituloResult = validation.validateTitulo(_state.value.titulo)
            val tipoTransaccionResult = validation.validateTipoTransaccion(_state.value.tipoTransaccion)
            val categoriaResult = validation.validateCategoria(_state.value.categoriaId)
            val monedaResult = validation.validateMoneda(_state.value.moneda)
            val precioResult = validation.validatePrecio(_state.value.precio)
            val ciudadResult = validation.validateCiudad(_state.value.ciudad)
            val estadoProvinciaResult = validation.validateEstadoProvincia(_state.value.estadoProvincia)
            val descripcionResult = validation.validateDescripcion(_state.value.descripcion)
            val metrosCuadradosResult = validation.validateMetrosCuadrados(_state.value.metrosCuadrados)
            val estadoPropiedadResult = validation.validateEstadoPropiedad(_state.value.estadoPropiedadId)

            _state.update {
                it.copy(
                    tituloError = if(!tituloResult.isValid) tituloResult.errorMessage else null,
                    tipoTransaccionError = if(!tipoTransaccionResult.isValid) tipoTransaccionResult.errorMessage else null,
                    categoriaError = if(!categoriaResult.isValid) categoriaResult.errorMessage else null,
                    monedaError = if(!monedaResult.isValid) monedaResult.errorMessage else null,
                    precioError = if(!precioResult.isValid) precioResult.errorMessage else null,
                    ciudadError = if(!ciudadResult.isValid) ciudadResult.errorMessage else null,
                    estadoProvinciaError = if(!estadoProvinciaResult.isValid) estadoProvinciaResult.errorMessage else null,
                    descripcionError = if(!descripcionResult.isValid) descripcionResult.errorMessage else null,
                    metrosCuadradosError = if(!metrosCuadradosResult.isValid) metrosCuadradosResult.errorMessage else null,
                )
            }
            return tituloResult.isValid && tipoTransaccionResult.isValid &&
                    categoriaResult.isValid && monedaResult.isValid &&
                    precioResult.isValid && ciudadResult.isValid &&
                    estadoProvinciaResult.isValid && descripcionResult.isValid &&
                    metrosCuadradosResult.isValid && estadoPropiedadResult.isValid
    }


    private suspend fun handleImageUpdates(propiedadId: Int) {
        if (_state.value.deleteImages.isNotEmpty()) {
            when (val result = deleteImagesUseCase(_state.value.deleteImages)) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Error al eliminar imágenes: ${result.message}"
                        )
                    }
                    return
                }
                else -> {}
            }
        }

        uploadImages(propiedadId)
    }

    private fun updateDeleteListImages(imagenId: Int) {
        viewModelScope.launch {
            val updatedDeleteList = _state.value.deleteImages.toMutableList()
            updatedDeleteList.add(imagenId)

            val updatedImagenesCargadas = _state.value.imagenesCargadas.toMutableList()
            updatedImagenesCargadas.removeAll { it.imagenId == imagenId }

            _state.update {
                it.copy(
                    deleteImages = updatedDeleteList,
                    imagenesCargadas = updatedImagenesCargadas
                )
            }
        }
    }

    private fun uploadImages(propiedadId: Int) {
        viewModelScope.launch {
            if (_state.value.selectedImages.isEmpty()){
                _state.update {
                    it.copy(
                        isLoading = false,
                        userMessage = "Propiedad guardada correctamente.",
                    )
                }
                clearForm()
                return@launch
            }

            when (val result = upLoadImagesUseCase(propiedadId, _state.value.selectedImages)) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Propiedad e imágenes guardadas correctamente.",
                        )
                    }
                    clearForm()
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Propiedad guardada, pero falló la subida de imágenes: ${result.message}",
                        )
                    }
                }
                else -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Error al guardar la propiedad",
                        )
                    }
                }
            }
        }
    }

    fun deleteSelectedImage(uri: Uri){
        viewModelScope.launch {
            val updatedList = _state.value.selectedImages.toMutableList()
            updatedList.remove(uri)
            _state.update { it.copy(selectedImages = updatedList) }
        }
    }

    fun seleccionarImagen(uris: List<Uri>){
        viewModelScope.launch {
            val updatedList = _state.value.selectedImages.toMutableList()
            val totalImagenes = _state.value.imagenesCargadas.size + updatedList.size

            if(totalImagenes + uris.size <= 10){
                updatedList.addAll(uris)
                _state.update { it.copy(selectedImages = updatedList) }
            } else {
                val espacioDisponible = 10 - totalImagenes
                if(espacioDisponible > 0){
                    updatedList.addAll(uris.take(espacioDisponible))
                    _state.update { it.copy(
                        selectedImages = updatedList,
                        userMessage = "Solo se pueden agregar hasta 10 imágenes. Se agregaron $espacioDisponible."
                    ) }
                } else {
                    _state.update { it.copy(
                        userMessage = "Ya has alcanzado el límite de 10 imágenes."
                    ) }
                }
            }
        }
    }

    fun clearForm(){
        _state.update {
            it.copy(
                titulo = "",
                precio = 0.0,
                moneda = "",
                ciudad = "",
                estadoProvincia = "",
                tipoTransaccion = "",
                categoriaId = 0,
                estadoPropiedadId = 0,
                fechaPublicacion = "",
                fechaActualizacion = "",
                descripcion = "",
                habitaciones = 0,
                banos = 0.0,
                parqueo = 0,
                metrosCuadrados = 0.0,
                selectedImages = emptyList(),
                imagenesCargadas = emptyList(),
                deleteImages = emptyList(),
                )
        }
    }

    fun deletePropiedad(propiedadId: Int){
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when(val result = deletePropiedadUseCase(propiedadId)){
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Propiedad eliminada correctamente",
                            showDeleteDialog = false
                        )
                    }
                    clearForm()
                }
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Error al eliminar la propiedad",
                            showDeleteDialog = false)
                    }
                }
                else -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userMessage = "Error al eliminar la propiedad",
                            showDeleteDialog = false)
                    }
                }
            }
        }
    }

}