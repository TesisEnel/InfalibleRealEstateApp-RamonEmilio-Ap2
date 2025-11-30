package com.infaliblerealestate.presentation.upsertPropiedad

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infaliblerealestate.presentation.util.navigation.Screen
import com.infaliblerealestate.ui.theme.InfalibleRealEstateTheme
import kotlin.toString

@Composable
fun UpsertPropiedadScreen(
    usuarioId: String?,
    propiedadId: Int? = null,
    viewModel: UpsertPropiedadViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            if (it.isNotBlank()) snack.showSnackbar(it)
            viewModel.onEvent(UpsertPropiedadUiEvent.UserMessageShown)
        }
    }

    LaunchedEffect(propiedadId) {
        propiedadId?.let {
            viewModel.onEvent(UpsertPropiedadUiEvent.LoadPropiedad(it))
        }
    }

    UpsertPropiedadContent(
        onEvent = viewModel::onEvent,
        snack = snack,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertPropiedadContent(
    onEvent: (UpsertPropiedadUiEvent) -> Unit,
    snack: SnackbarHostState,
    state: UpsertPropiedadUiState
) {
    var tipoTransaccionExpanded by remember { mutableStateOf(false) }
    var categoriaExpanded by remember { mutableStateOf(false) }
    var monedaExpanded by remember { mutableStateOf(false) }
    var estadoExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                navigationIcon = {
                    IconButton(
                        onClick = {

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
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                },
                title = {
                    Text(
                        text = if (state.isEditing) "Editar Propiedad" else "Nueva Propiedad",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snack,
                modifier = Modifier.padding(bottom = 96.dp)
            )
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(vertical = 8.dp)
                    .drawBehind {
                        val stroke = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                        val radius = 12.dp.toPx()
                        drawRoundRect(
                            color = Color(0xFFAFBED0),
                            style = stroke,
                            cornerRadius = CornerRadius(radius, radius)
                        )
                    }
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Transparent)
                    .clickable {  },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Icon(
                            imageVector = Icons.Default.AddToPhotos,
                            contentDescription = "Subir imagenes",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Subir imágenes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            OutlinedTextField(
                value = state.titulo,
                onValueChange = { onEvent(UpsertPropiedadUiEvent.OnTituloChanged(it)) },
                label = { Text("Título") },
                placeholder = { Text("Ingrese el título de la propiedad") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = tipoTransaccionExpanded,
                onExpandedChange = { tipoTransaccionExpanded = !tipoTransaccionExpanded }
            ) {
                OutlinedTextField(
                    value = state.tipoTransaccion,
                    onValueChange = { onEvent(UpsertPropiedadUiEvent.OnTipoTransaccionChanged(it)) },
                    readOnly = true,
                    label = { Text("Tipo de Transacción") },
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        Icon(if(tipoTransaccionExpanded)Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown, contentDescription = "Opciones de tipo de transacción")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = tipoTransaccionExpanded,
                    onDismissRequest = { tipoTransaccionExpanded = false }
                ) {
                    listOf("Venta", "Alquiler").forEach { tipoTransaccion ->
                        DropdownMenuItem(
                            text = { Text(tipoTransaccion) },
                            onClick = {
                                onEvent(UpsertPropiedadUiEvent.OnTipoTransaccionChanged(tipoTransaccion))
                                tipoTransaccionExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = categoriaExpanded,
                onExpandedChange = { categoriaExpanded = !categoriaExpanded }
            ) {
                OutlinedTextField(
                    value = state.categoria,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Tipo de Propiedad") },
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        Icon(if(categoriaExpanded)Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown, contentDescription = "Opciones de categoria")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = categoriaExpanded,
                    onDismissRequest = { categoriaExpanded = false }
                ) {
                    state.categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = { Text(categoria.nombreCategoria.toString()) },
                            onClick = {
                                onEvent(UpsertPropiedadUiEvent.OnCategoriaIdChanged(categoria.categoriaId, categoria.nombreCategoria ?: ""))
                                categoriaExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ExposedDropdownMenuBox(
                    expanded = monedaExpanded,
                    modifier = Modifier.weight(1f),
                    onExpandedChange = { monedaExpanded = !monedaExpanded }
                ) {
                    OutlinedTextField(
                        value = state.moneda,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Moneda") },
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            Icon(if(monedaExpanded)Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown, contentDescription = "Opciones de moneda")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = monedaExpanded,
                        onDismissRequest = { monedaExpanded = false }
                    ) {
                        listOf("USD$", "DOP$").forEach { moneda ->
                            DropdownMenuItem(
                                text = { Text(moneda) },
                                onClick = {
                                    onEvent(UpsertPropiedadUiEvent.OnMonedaChanged(moneda))
                                    monedaExpanded = false
                                }
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = state.precio.toString(),
                    onValueChange = { onEvent(UpsertPropiedadUiEvent.OnPrecioChanged(it.toDoubleOrNull() ?: 0.0)) },
                    label = { Text("Precio") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.ciudad,
                onValueChange = { onEvent(UpsertPropiedadUiEvent.OnCiudadChanged(it)) },
                label = { Text("Ciudad") },
                placeholder = { Text("Ingrese la ciudad donde está ubicada") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.estadoProvincia,
                onValueChange = { onEvent(UpsertPropiedadUiEvent.OnEstadoProvinciachanged(it)) },
                label = { Text("Provincia") },
                placeholder = { Text("Ingrese la provincia donde está ubicada") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.habitaciones.toString(),
                    onValueChange = { onEvent(UpsertPropiedadUiEvent.OnHabitacionesChanged(it.toIntOrNull() ?: 0)) },
                    label = { Text("Habitaciones") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = state.banos.toString(),
                    onValueChange = { onEvent(UpsertPropiedadUiEvent.OnBanosChanged(it.toDoubleOrNull() ?: 0.0)) },
                    label = { Text("Baños") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.parqueo.toString(),
                    onValueChange = { onEvent(UpsertPropiedadUiEvent.OnParqueoChanged(it.toIntOrNull() ?: 0)) },
                    label = { Text("Parqueos") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = state.metrosCuadrados.toString(),
                    onValueChange = { onEvent(UpsertPropiedadUiEvent.OnMetrosCuadradosChanged(it.toDoubleOrNull() ?: 0.0)) },
                    label = { Text("Metros") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.descripcion,
                onValueChange = { onEvent(UpsertPropiedadUiEvent.OnDescripcionChanged(it)) },
                label = { Text("Descripción") },
                placeholder = { Text("Ingrese la descripción de la propiedad") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 6,
                shape = RoundedCornerShape(8.dp),
            )

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = estadoExpanded,
                onExpandedChange = { estadoExpanded = !estadoExpanded }
            ) {
                OutlinedTextField(
                    value = state.estadoPropiedad,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Estado de la propiedad") },
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        Icon(if(estadoExpanded)Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown, contentDescription = "Opciones de estados")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = estadoExpanded,
                    onDismissRequest = { estadoExpanded = false }
                ) {
                    state.estadosPropiedades.forEach { estado ->
                        DropdownMenuItem(
                            text = { Text(estado.nombreEstado.toString()) },
                            onClick = {
                                onEvent(UpsertPropiedadUiEvent.OnEstadoPropiedadIdChanged(estado.estadoPropiedadId, estado.nombreEstado ?: ""))
                                estadoExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = {onEvent(UpsertPropiedadUiEvent.ClearForm)},
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Limpiar")
                }
                Button(
                    onClick = { onEvent(UpsertPropiedadUiEvent.SavePropiedad) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Guardar", color = Color.White)
                }
            }
        }

    }

}


@Preview(showBackground = true)
@Composable
fun UpsertPropiedadContentPreview() {
    InfalibleRealEstateTheme() {
        UpsertPropiedadContent(
            onEvent = {},
            snack = remember { SnackbarHostState() },
            state = UpsertPropiedadUiState()
        )
    }
}