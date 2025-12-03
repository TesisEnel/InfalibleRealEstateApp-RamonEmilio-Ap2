package com.infaliblerealestate.presentation.upsertPropiedad

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddToPhotos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.infaliblerealestate.dominio.model.ImagenPropiedad
import com.infaliblerealestate.presentation.util.components.ThemedSnackbarHost
import com.infaliblerealestate.ui.theme.InfalibleRealEstateTheme
import kotlin.toString

@Composable
fun UpsertPropiedadScreen(
    usuarioId: String?,
    isAdmin: Boolean = false,
    propiedadId: Int? = null,
    viewModel: UpsertPropiedadViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(state.shouldNavigateBack) {
        if (state.shouldNavigateBack) {
            onNavigateBack()
        }
    }

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

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            viewModel.onEvent(UpsertPropiedadUiEvent.OnImagesSelected (uris))
        }
    }

    UpsertPropiedadContent(
        onEvent = viewModel::onEvent,
        snack = snack,
        state = state,
        onImagePickerClick = { imagePickerLauncher.launch("image/*") },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UpsertPropiedadContent(
    onEvent: (UpsertPropiedadUiEvent) -> Unit,
    snack: SnackbarHostState,
    state: UpsertPropiedadUiState,
    onImagePickerClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var tipoTransaccionExpanded by remember { mutableStateOf(false) }
    var categoriaExpanded by remember { mutableStateOf(false) }
    var monedaExpanded by remember { mutableStateOf(false) }
    var estadoExpanded by remember { mutableStateOf(false) }
    var ciudadExpanded by remember { mutableStateOf(false) }
    var provinciaExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
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
            ThemedSnackbarHost(
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
            if(state.isLoading){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularWavyProgressIndicator()
                }
            }else{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
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
                        .clickable { onImagePickerClick() },
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
                            modifier = Modifier.size(35.dp)
                        )


                        Text(
                            text = "Subir imágenes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                    }
                }

                ImageGallerySection(
                    imagenesCargadas = state.imagenesCargadas,
                    selectedImages = state.selectedImages,
                    onDeleteServerImage = {onEvent(UpsertPropiedadUiEvent.OnDeleteServerImage(it))},
                    onDeleteSelectedImage = { uri ->
                        onEvent(UpsertPropiedadUiEvent.OnDeleteSelectedImage(uri))
                    }
                )

                OutlinedTextField(
                    value = state.titulo,
                    onValueChange = { onEvent(UpsertPropiedadUiEvent.OnTituloChanged(it)) },
                    label = { Text("Título") },
                    placeholder = { Text("Ingrese el título de la propiedad") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    isError = state.tituloError != null,
                    supportingText = {
                        if(state.tituloError != null){
                            Text(text = state.tituloError , color = MaterialTheme.colorScheme.error)
                        }
                    }
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
                            .menuAnchor(),
                        isError = state.tipoTransaccionError != null,
                        supportingText = {
                            if(state.tipoTransaccionError != null){
                                Text(text = state.tipoTransaccionError , color = MaterialTheme.colorScheme.error)
                            }
                        }
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
                            .menuAnchor(),
                        isError = state.categoriaError != null,
                        supportingText = {
                            if(state.categoriaError != null){
                                Text(text = state.categoriaError , color = MaterialTheme.colorScheme.error)
                            }
                        }
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
                                .menuAnchor(),
                            isError = state.monedaError != null,
                            supportingText = {
                                if(state.monedaError != null){
                                    Text(text = state.monedaError , color = MaterialTheme.colorScheme.error)
                                }
                            }
                        )
                        ExposedDropdownMenu(
                            expanded = monedaExpanded,
                            onDismissRequest = { monedaExpanded = false }
                        ) {
                            listOf("Peso", "Dolar").forEach { moneda ->
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.precioError != null,
                        supportingText = {
                            if(state.precioError != null){
                                Text(text = state.precioError , color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = ciudadExpanded && state.filteredCiudades.isNotEmpty(),
                    onExpandedChange = { }
                ) {
                    OutlinedTextField(
                        value = state.ciudad,
                        onValueChange = {
                            onEvent(UpsertPropiedadUiEvent.OnCiudadChanged(it))
                            ciudadExpanded = it.isNotEmpty()
                        },
                        label = { Text("Ciudad") },
                        placeholder = { Text("Ingrese la ciudad donde está ubicada") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(8.dp),
                        isError = state.ciudadError != null,
                        supportingText = {
                            if(state.ciudadError != null){
                                Text(text = state.ciudadError , color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    if (state.filteredCiudades.isNotEmpty()) {
                        ExposedDropdownMenu(
                            expanded = ciudadExpanded,
                            onDismissRequest = { ciudadExpanded = false }
                        ) {
                            state.filteredCiudades.take(5).forEach { ciudad ->
                                DropdownMenuItem(
                                    text = { Text(ciudad) },
                                    onClick = {
                                        onEvent(UpsertPropiedadUiEvent.OnCiudadChanged(ciudad))
                                        ciudadExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = provinciaExpanded && state.filteredProvincias.isNotEmpty(),
                    onExpandedChange = { }
                ) {
                    OutlinedTextField(
                        value = state.estadoProvincia,
                        onValueChange = {
                            onEvent(UpsertPropiedadUiEvent.OnEstadoProvinciachanged(it))
                            provinciaExpanded = it.isNotEmpty()
                        },
                        label = { Text("Provincia") },
                        placeholder = { Text("Ingrese la provincia donde está ubicada") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(8.dp),
                        isError = state.estadoProvinciaError != null,
                        supportingText = {
                            if(state.estadoProvinciaError != null){
                                Text(text = state.estadoProvinciaError , color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )

                    if (state.filteredProvincias.isNotEmpty()) {
                        ExposedDropdownMenu(
                            expanded = provinciaExpanded,
                            onDismissRequest = { provinciaExpanded = false }
                        ) {
                            state.filteredProvincias.take(5).forEach { provincia ->
                                DropdownMenuItem(
                                    text = { Text(provincia) },
                                    onClick = {
                                        onEvent(UpsertPropiedadUiEvent.OnEstadoProvinciachanged(provincia))
                                        provinciaExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.metrosCuadradosError != null,
                        supportingText = {
                            if(state.metrosCuadradosError != null){
                                Text(text = state.metrosCuadradosError , color = MaterialTheme.colorScheme.error)
                            }
                        }
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
                        .height(200.dp),
                    maxLines = 6,
                    shape = RoundedCornerShape(8.dp),
                    isError = state.descripcionError != null,
                    supportingText = {
                        if(state.descripcionError != null){
                            Text(text = state.descripcionError , color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                ExposedDropdownMenuBox(
                    expanded = estadoExpanded,
                    onExpandedChange = { estadoExpanded = !estadoExpanded }
                ) {
                    OutlinedTextField(
                        value = state.estadoPropiedad,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado de la propiedad") },
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            Icon(if(estadoExpanded)Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown, contentDescription = "Opciones de estados")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        isError = state.estadoPropiedadError != null,
                        supportingText = {
                            if(state.estadoPropiedadError != null){
                                Text(text = state.estadoPropiedadError , color = MaterialTheme.colorScheme.error)
                            }
                        }
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
                        modifier = Modifier.weight(.5f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary,
                            containerColor = Color.Transparent
                        )
                    ) {
                        Icon(imageVector = Icons.Default.CleaningServices, contentDescription = "Volver")

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
                    if(state.isEditing){
                        OutlinedButton(
                            onClick = {onEvent(UpsertPropiedadUiEvent.ShowDeleteDialog)},
                            modifier = Modifier.weight(.5f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                }
            }
        }

    }
    if(state.showDeleteDialog) {
        deleteDialog(
            onDismiss = { onEvent(UpsertPropiedadUiEvent.HideDeleteDialog) },
            onConfirm = { onEvent(UpsertPropiedadUiEvent.OnDeletePropiedad(state.propiedadAEditar?.propiedadId ?: 0)) }
        )
    }

}

@Composable
fun deleteDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
){
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Eliminar propiedad",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
                ) },
        text = { Text(text = "¿Esta seguro que desea eliminar esta propiedad?") },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()

            ) {
                OutlinedButton(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                        containerColor = Color.Transparent
                    )
                ){
                    Text("Cancelar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onConfirm() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ){
                    Text("Eliminar")
                }
            }
        }
    )

}

@Composable
fun ImageGallerySection(
    imagenesCargadas: List<ImagenPropiedad>,
    selectedImages: List<Uri>,
    onDeleteServerImage: (Int) -> Unit,
    onDeleteSelectedImage: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    if (imagenesCargadas.isNotEmpty() || selectedImages.isNotEmpty()) {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                count = imagenesCargadas.size,
                key = { index -> "cargada_${imagenesCargadas[index].imagenId}" }
            ) { index ->
                ImageItem(
                    imageUrl = imagenesCargadas[index].urlImagen,
                    onDelete = { onDeleteServerImage(imagenesCargadas[index].imagenId) },
                    contentDescription = "Imagen cargada"
                )
            }

            items(
                count = selectedImages.size,
                key = { index -> "nueva_${selectedImages[index]}" }
            ) { index ->
                ImageItem(
                    imageUrl = selectedImages[index],
                    onDelete = { onDeleteSelectedImage(selectedImages[index]) },
                    contentDescription = "Imagen seleccionada"
                )
            }
        }
    }
}

@Composable
fun ImageItem(
    imageUrl: Any,
    onDelete: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(24.dp)
                .background(
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Eliminar imagen",
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.size(16.dp)
            )
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
            state = UpsertPropiedadUiState(),
            onImagePickerClick = {},
            onNavigateBack = {}
        )
    }
}