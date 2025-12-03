package com.infaliblerealestate.presentation.upsertPropiedad

import android.net.Uri

interface UpsertPropiedadUiEvent {
    data class LoadPropiedad(val id: Int) : UpsertPropiedadUiEvent
    data class OnDeletePropiedad(val propiedadId: Int) : UpsertPropiedadUiEvent
    data class OnDeleteServerImage(val imagenId: Int) : UpsertPropiedadUiEvent
    data object ShowDeleteDialog : UpsertPropiedadUiEvent
    data object HideDeleteDialog : UpsertPropiedadUiEvent
    data object UserMessageShown : UpsertPropiedadUiEvent
    data object SavePropiedad : UpsertPropiedadUiEvent
    data object ClearForm : UpsertPropiedadUiEvent
    data class OnImagesSelected(val uris: List<Uri>) : UpsertPropiedadUiEvent
    data class OnDeleteSelectedImage(val uri: Uri) : UpsertPropiedadUiEvent
    data class OnTituloChanged(val titulo: String) : UpsertPropiedadUiEvent
    data class OnPrecioChanged(val precio: Double) : UpsertPropiedadUiEvent
    data class OnMonedaChanged(val moneda: String) : UpsertPropiedadUiEvent
    data class OnCiudadChanged(val ciudad: String) : UpsertPropiedadUiEvent
    data class OnEstadoProvinciachanged(val estadoPropiedadProvincia: String) : UpsertPropiedadUiEvent
    data class OnTipoTransaccionChanged(val tipoTransaccion: String) : UpsertPropiedadUiEvent
    data class OnCategoriaIdChanged(val categoriaId: Int, val categoria: String) : UpsertPropiedadUiEvent
    data class OnEstadoPropiedadIdChanged(val estadoPropiedadId: Int, val estado: String) : UpsertPropiedadUiEvent
    data class OnDescripcionChanged(val descripcion: String) : UpsertPropiedadUiEvent
    data class OnHabitacionesChanged(val habitaciones: Int) : UpsertPropiedadUiEvent
    data class OnBanosChanged(val banos: Double) : UpsertPropiedadUiEvent
    data class OnParqueoChanged(val parqueo: Int) : UpsertPropiedadUiEvent
    data class OnMetrosCuadradosChanged(val metrosCuadrados: Double) : UpsertPropiedadUiEvent
}