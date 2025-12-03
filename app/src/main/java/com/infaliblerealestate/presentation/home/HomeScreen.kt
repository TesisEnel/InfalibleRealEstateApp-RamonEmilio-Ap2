package com.infaliblerealestate.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Villa
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infaliblerealestate.presentation.util.components.PropiedadChip
import com.infaliblerealestate.presentation.util.components.PropiedadItem
import com.infaliblerealestate.presentation.util.components.SheetPropiedadDetalle
import com.infaliblerealestate.presentation.util.components.ThemedSnackbarHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCatalogo: (String) -> Unit,
    onNavigateToUpsertPropiedad: (String?, Int?, Boolean) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            if(it.isNotBlank()) snack.showSnackbar(it)
            viewModel.onEvent(HomeUiEvent.UserMessageShown)
        }
    }

    HomeScreenContent(
        state,
        viewModel::onEvent,
        snack = snack,
        onNavigateToCatalogo = onNavigateToCatalogo,
        onNavigateToUpsertPropiedad = onNavigateToUpsertPropiedad
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreenContent(
    state: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    snack: SnackbarHostState,
    onNavigateToCatalogo: (String) -> Unit,
    onNavigateToUpsertPropiedad: (String?, Int?, Boolean) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "¡Encuentra tu nuevo hogar!",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        modifier = Modifier.padding(top = 0.dp)
                    )
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                ),
                windowInsets = WindowInsets(top = 0.dp)
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
            isRefreshing = state.isLoading,
            onRefresh = { onEvent(HomeUiEvent.LoadInitialData)},
            state = pullToRefreshState,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            indicator = {
                PullToRefreshDefaults.LoadingIndicator(
                    state = pullToRefreshState,
                    isRefreshing = state.isLoading ,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                if (state.isLoading && state.categorias.isEmpty()) {
                    item {
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
                    }
                } else {
                    item {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(state.categorias) { categoria ->
                                val icon = when (categoria.nombreCategoria) {
                                    "Casa" -> Icons.Default.Home
                                    "Apartamento" -> Icons.Default.Business
                                    "Villa" -> Icons.Default.Villa
                                    "Penthouse" -> Icons.Default.Apartment
                                    "Terreno" -> Icons.Default.Map
                                    else -> Icons.Default.Store
                                }

                                PropiedadChip(
                                    icon = icon,
                                    text = categoria.nombreCategoria.toString(),
                                    onClick = {
                                        onNavigateToCatalogo(categoria.nombreCategoria.toString())
                                    },
                                    modifier = Modifier.height(60.dp)
                                )
                            }
                        }
                    }

                    if (state.propiedades.isEmpty() && !state.isLoading) {
                        item {
                            Text(
                                text = "No se encontraron propiedades para mostrar",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else if (state.propiedades.isNotEmpty()) {
                        item {
                            Text(
                                text = "Últimas propiedades",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        items(
                            items = state.propiedades,
                            key = { propiedad -> propiedad.propiedadId }
                        ) { propiedad ->
                            PropiedadItem(
                                propiedad = propiedad,
                                onClick = { onEvent(HomeUiEvent.LoadPropiedad(propiedad.propiedadId)) },
                                modifier = Modifier.padding(horizontal = 8.dp),
                                onAddToCart = { onEvent(HomeUiEvent.AddToCart(propiedad)) },
                                onEdit = {
                                    onNavigateToUpsertPropiedad(state.usuarioId, propiedad.propiedadId, state.isAdmin )
                                },
                                showAdminOptions = state.isAdmin
                            )
                        }
                    }
                }
            }
        }
    }

    if (state.showSheet) {
        SheetPropiedadDetalle(
            propiedad = state.propiedad,
            onDismiss = { onEvent(HomeUiEvent.HideSheet) }
        )
    }
}



