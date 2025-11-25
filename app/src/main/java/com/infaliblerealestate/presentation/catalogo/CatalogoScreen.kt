package com.infaliblerealestate.presentation.catalogo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infaliblerealestate.dominio.model.Propiedades
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Villa
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.infaliblerealestate.dominio.model.ImagenPropiedad
import com.infaliblerealestate.dominio.model.PropiedadesDetalle
import com.infaliblerealestate.presentation.util.components.PropiedadItem
import com.infaliblerealestate.presentation.util.components.PropiedadChip
import com.infaliblerealestate.presentation.util.components.SheetPropiedadDetalle
import com.infaliblerealestate.ui.theme.InfalibleRealEstateTheme

@Composable
fun CatalogoScreen(
    usuarioId: Int?,
    viewModel: CatalogoViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember{ SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            if(it.isNotBlank()) snack.showSnackbar(it)
            viewModel.onEvent(CatalogoUiEvent.userMessageShown)
        }
    }

    CatalogoContent(
        onEvent = viewModel::onEvent,
        snack = snack,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CatalogoContent(
    onEvent: (CatalogoUiEvent) -> Unit,
    snack: SnackbarHostState,
    state: CatalogoUiState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                title = {
                    Text(
                        text = "Catálogo",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(CatalogoUiEvent.showFilterDialog) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snack,
                modifier = Modifier.padding(bottom = 96.dp)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            if(state.isLoading){
                CircularWavyProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = state.infoMessage ?: "Cargando...",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant


                )
            }else {
                val propiedades = state.propiedades
                if(propiedades.isEmpty()){
                    Text(
                        text = "No hay propiedades disponibles",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        items(
                            items = propiedades,
                            key = { propiedad -> propiedad.propiedadId }
                        ) { propiedad ->
                            PropiedadItem(
                                propiedad = propiedad,
                                onClick = { onEvent(CatalogoUiEvent.loadPropiedad(propiedad.propiedadId)) }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(90.dp))
        }
    }
    if(state.showFilterDialog){
        FilterDialog(
            state = state,
            onEvent = onEvent
        )
    }
    if (state.showSheet) {
        SheetPropiedadDetalle(
            propiedad = state.propiedad,
            onDismiss = { onEvent(CatalogoUiEvent.hideSheet) }
        )
    }
}

@Composable
fun FilterDialog(
    state: CatalogoUiState,
    modifier: Modifier = Modifier,
    onEvent: (CatalogoUiEvent) -> Unit
){
    AlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {

                    Text(
                        text = "Seleccione los filtros que desea aplicar:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = {onEvent(CatalogoUiEvent.hideFilterDialog)},

                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PropiedadChip(
                        icon = Icons.Default.Home,
                        text = "Casa",
                        onClick = { onEvent(CatalogoUiEvent.onFilterCasa(!state.filtroCasa)) },
                        selected = state.filtroCasa
                    )
                    PropiedadChip(
                        icon = Icons.Default.Business,
                        text = "Apartamento",
                        onClick = { onEvent(CatalogoUiEvent.onFilterDepartamento(!state.filtroDepartamento)) },
                        selected = state.filtroDepartamento
                    )

                    PropiedadChip(
                        icon = Icons.Default.Villa,
                        text = "Villa",
                        onClick = { onEvent(CatalogoUiEvent.onFilterVilla(!state.filtroVilla)) },
                        selected = state.filtroVilla
                    )

                    PropiedadChip(
                        icon = Icons.Default.Apartment,
                        text = "Penthouse",
                        onClick = { onEvent(CatalogoUiEvent.onFilterPenthouse(!state.filtroPenthouse)) },
                        selected = state.filtroPenthouse
                    )

                    PropiedadChip(
                        icon = Icons.Default.LocationOn,
                        text = "Terreno",
                        onClick = { onEvent(CatalogoUiEvent.onFilterSolar(!state.filtroSolar)) },
                        selected = state.filtroSolar
                    )

                    PropiedadChip(
                        icon = Icons.Default.Store,
                        text = "Local Comercial",
                        onClick = { onEvent(CatalogoUiEvent.onFilterLocalComercial(!state.filtroLocalComercial)) },
                        selected = state.filtroLocalComercial
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = { onEvent(CatalogoUiEvent.showAllFilters)}
                ) {
                    Text(
                        text = if(state.showAllFilters)"Ocultar" else "Mostrar todos los filtros",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                if (state.showAllFilters){
                    Slider(
                        value = state.filtroPrecio,
                        onValueChange = {onEvent(CatalogoUiEvent.onFilterPrecio(it))},
                        valueRange = 0f..30000000f
                    )
                    Text(text = "Precio: ${String.format("%,.2f", state.filtroPrecio)}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PropiedadChip(
                            icon = Icons.Filled.Sell,
                            onClick = { onEvent(CatalogoUiEvent.onFilterVenta(!state.filtroVenta)) },
                            text = "Venta",
                            selected = state.filtroVenta
                        )
                        PropiedadChip(
                            icon = Icons.Outlined.Handshake,
                            onClick = { onEvent(CatalogoUiEvent.onFilterAlquiler(!state.filtroAlquiler)) },
                            text = "Alquiler",
                            selected = state.filtroAlquiler
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Stepper(
                            value = state.filtroHabitaciones,
                            onValueChange = { onEvent(CatalogoUiEvent.onFilterHabitaciones(it)) },
                            text = "Habitaciones"
                        )

                        Stepper(
                            value = state.filtroBanos,
                            onValueChange = { onEvent(CatalogoUiEvent.onFilterBanos(it)) },
                            text = "Baños"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Stepper(
                            value = state.filtroParqueos,
                            onValueChange = { onEvent(CatalogoUiEvent.onFilterParqueos(it)) },
                            text = "Parqueos"
                        )
                    }
                }
            }

        },

        onDismissRequest = { onEvent(CatalogoUiEvent.hideFilterDialog) },

        confirmButton = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)

                ) {
                    Button(
                        onClick = { onEvent(CatalogoUiEvent.clearFilters) }
                    ){
                        Text("Limpiar")
                    }
                    Button(
                        onClick = { onEvent(CatalogoUiEvent.filterPropiedades) }
                    ){
                        Text("Aplicar Filtros")
                    }
                }
        }
    )
}

@Composable
fun Stepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    text: String
) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            FilledTonalIconButton(
                onClick = {
                    val nuevo = (value - 1).coerceAtLeast(0)
                    onValueChange(nuevo)
                },
                enabled = value > 0
            ) {
                Icon(
                    imageVector = Icons.Filled.Remove,
                    contentDescription = "Restar"
                )
            }

            Text(
                text = "$value",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .widthIn(min = 40.dp)
                    .padding(horizontal = 8.dp)
            )

            FilledTonalIconButton(
                onClick = {
                    val nuevo = value + 1.coerceAtMost(10)
                    onValueChange(nuevo)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Añadir"
                )
            }
        }

}

@Preview
@Composable
fun CatalogoContentPreview() {
    InfalibleRealEstateTheme {
        CatalogoContent(
            onEvent = {},
            snack = remember { SnackbarHostState() },
            state = CatalogoUiState(
                propiedades = listOf(
                    Propiedades(
                        propiedadId = 1,
                        titulo = "Hermosa casa en venta",
                        precio = 250000.0,
                        moneda = "USD",
                        ciudad = "San Francisco de Macoris",
                        estadoProvincia = "Duarte",
                        tipoTransaccion = "Venta",
                        categoriaId = 1,
                        fechaPublicacion = "2023-01-01",
                        fechaActualizacion = "2023-01-10",
                        estadoPropiedadId = 1,
                        detalle = PropiedadesDetalle(
                            propiedadId = 1,
                            descripcion = "Una hermosa casa con todas las comodidades.",
                            habitaciones = 3,
                            banos = 2.0,
                            parqueo = 1,
                            metrosCuadrados = 120.0
                        ),
                        imagenes = listOf(
                            ImagenPropiedad(
                                imagenId = 1,
                                propiedadId = 1,
                                urlImagen = "https://scrgajdklzafafwrxrng.supabase.co/storage/v1/object/public/propiedades/afc5c91c-521d-45f6-8f73-eeba37ea2b43_Casa.jpg",
                                orden = 1
                            )
                        )
                    )
                ),
                showSheet = false,
                propiedad = null
            )
        )
    }
}





