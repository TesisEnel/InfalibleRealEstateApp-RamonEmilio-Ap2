package com.infaliblerealestate.presentation.carrito

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Whatsapp
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infaliblerealestate.dominio.model.CarritoItem
import com.infaliblerealestate.dominio.model.ImagenPropiedad
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.model.PropiedadesDetalle
import com.infaliblerealestate.presentation.util.components.CarritoItem
import com.infaliblerealestate.presentation.util.components.SheetPropiedadDetalle
import com.infaliblerealestate.presentation.util.components.ThemedSnackbarHost

@Composable
fun CarritoScreen(
    usuarioId: String?,
    viewModel: CarritoViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember{ SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            if(it.isNotBlank()) snack.showSnackbar(it)
            viewModel.onEvent(CarritoUiEvent.UserMessageShown)
        }
    }

    LaunchedEffect(state.whatsappUrl) {
        state.whatsappUrl?.let { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            viewModel.onEvent(CarritoUiEvent.WhatsappLaunched)
        }
    }

    CarritoContent(
        state,
        snack = snack,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CarritoContent(
    state: CarritoUiState,
    snack: SnackbarHostState,
    onEvent: (CarritoUiEvent) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                title = {
                    Text(
                        text = "Carrito",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(CarritoUiEvent.SolicitarCompra)
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Whatsapp,
                            contentDescription = "Enviar Carrito",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                },
            )
        },

        snackbarHost = {
            ThemedSnackbarHost(
                hostState = snack,
                modifier = Modifier.padding(bottom = 96.dp)
            )
        },
    ) { padding ->

        PullToRefreshBox(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            isRefreshing = state.isLoading,
            state = pullToRefreshState,
            onRefresh = { onEvent(CarritoUiEvent. LoadCarrito)},
            indicator = {
                PullToRefreshDefaults.LoadingIndicator(
                    state = pullToRefreshState,
                    isRefreshing = state.isLoading,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if(state.usuarioId == null){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Inicie Sesion para agregar propiedades al carrito",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                }else if(state.isLoading){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(90.dp))
                        Text(
                            text = "Cargando...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                } else if(state.items.isEmpty()){
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hay propiedades en el carrito",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    val items = state.items
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(
                            items = items,
                            key = { item -> item.propiedadId }
                        ) { item ->
                            CarritoItem(
                                propiedad = item.propiedad,
                                onClick = { onEvent(CarritoUiEvent.LoadPropiedad(item.propiedadId)) },
                                onRemoveFromCart = { onEvent(CarritoUiEvent.DeletePropiedadDeCarrito(item.propiedadId)) }
                            )
                        }
                    }
                }
            }
        }
    }

    if(state.showSheet){
        SheetPropiedadDetalle(
            propiedad = state.propiedad,
            onDismiss = { onEvent(CarritoUiEvent.HideSheet) }
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CarritoContentPreview() {
    val detalleDemo = PropiedadesDetalle(
        propiedadId = 1,
        descripcion = "Apartamento moderno y bien ubicado.",
        habitaciones = 3,
        banos = 2.0,
        parqueo = 1,
        metrosCuadrados = 95.0
    )

    val imagenesDemo = listOf(
        ImagenPropiedad(
            imagenId = 1,
            propiedadId = 1,
            urlImagen = "https://via.placeholder.com/600x400",
            orden = 1
        )
    )

    val propiedadDemo = Propiedades(
        propiedadId = 1,
        administradorId = "admin-001",
        titulo = "Apartamento en el centro",
        precio = 2_500_000.0,
        moneda = "DOP",
        ciudad = "Santo Domingo",
        estadoProvincia = "Distrito Nacional",
        tipoTransaccion = "Venta",
        categoriaId = 1,
        fechaPublicacion = "2025-01-01",
        fechaActualizacion = "2025-01-10",
        estadoPropiedadId = 1,
        detalle = detalleDemo,
        imagenes = imagenesDemo
    )

    val stateDemo = CarritoUiState(
        usuarioId = "user-123",
        isLoading = false,
        items = listOf(
            CarritoItem(
                carritoId = 1,
                carritoItemId = 23,
                propiedadId = propiedadDemo.propiedadId,
                propiedad = propiedadDemo
            )
        ),
        showSheet = false,
        propiedad = propiedadDemo,
        userMessage = null,
        whatsappUrl = null
    )

    CarritoContent(
        state = stateDemo,
        snack = SnackbarHostState(),
        onEvent = {}
    )
}