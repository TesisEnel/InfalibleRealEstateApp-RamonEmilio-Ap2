package com.infaliblerealestate.presentation.upsertPropiedad

interface UpsertPropiedadUiEvent {
    data class LoadPropiedad(val id: Int) : UpsertPropiedadUiEvent
    data object UserMessageShown : UpsertPropiedadUiEvent
    data object SavePropiedad : UpsertPropiedadUiEvent
    data object ClearForm : UpsertPropiedadUiEvent
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