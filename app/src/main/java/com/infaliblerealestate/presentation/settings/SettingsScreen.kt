package com.infaliblerealestate.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.infaliblerealestate.dominio.model.Usuario
import com.infaliblerealestate.presentation.util.components.ThemedSnackbarHost
import com.infaliblerealestate.ui.theme.InfalibleRealEstateTheme

@Composable
fun SettingsScreen(
    onNavigateToLogin: () -> Unit,
    usuarioId: String?,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snack = remember { SnackbarHostState() }

    LaunchedEffect(state.userMessage) {
        state.userMessage?.let {
            if (it.isNotBlank()) snack.showSnackbar(it)
            viewModel.onEvent(SettingsUiEvent.UserMessageShown)
        }
    }

    SettingsContent(
        onNavigateToLogin = onNavigateToLogin,
        onEvent = viewModel::onEvent,
        state = state,
        snack = snack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onNavigateToLogin: () -> Unit,
    onEvent: (SettingsUiEvent) -> Unit,
    state: SettingsUiState,
    snack: SnackbarHostState
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ajustes",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineMedium,
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

    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.usuario != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Información del Usuario",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        IconButton(onClick = { onEvent(SettingsUiEvent.ShowDialog)}) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            state.usuario?.let{usuario ->
                                UserInfoField(
                                    label = "Nombre",
                                    value = usuario.nombre
                                )

                                UserInfoField(
                                    label = "Apellido",
                                    value = usuario.apellido
                                )

                                UserInfoField(
                                    label = "Correo electrónico",
                                    value = usuario.email
                                )

                                UserInfoField(
                                    label = "Teléfono",
                                    value = usuario.phoneNumber
                                )

                                Text(
                                    text = "Estado de la cuenta",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        text = usuario.estadoUsuario,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }

                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                OutlinedButton(
                    onClick = {
                        onEvent(SettingsUiEvent.Logout)
                        onNavigateToLogin()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if(state.usuario != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 2.dp,
                        brush = androidx.compose.ui.graphics.SolidColor(if(state.usuario != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                    )
                ) {
                    Text(
                        text = if (state.usuario != null) "Cerrar Sesión" else "Iniciar Sesión",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    if(state.showDialog){
        editDialog(
            onEvent = onEvent,
            state = state
        )
    }
}

@Composable
fun UserInfoField(
    label: String,
    value: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun editDialog(
    onEvent: (SettingsUiEvent) -> Unit,
    state: SettingsUiState,
){

    AlertDialog(
        onDismissRequest = {onEvent(SettingsUiEvent.HideDialog)},
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = "Editar Perfil",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.usuarioEditado?.nombre.toString(),
                    onValueChange = {onEvent(SettingsUiEvent.OnNombreChanged(it))},
                    label = { Text("Nombre") },
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("Ej: Ana") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.nombreError != null,
                    supportingText = {
                        if (state.nombreError != null){
                            Text(
                                text = state.nombreError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                )
                OutlinedTextField(
                    value = state.usuarioEditado?.apellido.toString(),
                    onValueChange ={ onEvent(SettingsUiEvent.OnApellidoChanged(it))},
                    label = { Text("Apellido") },
                    placeholder = { Text("Ej: García") },
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.apellidoError != null,
                    supportingText = {
                        if (state.apellidoError != null){
                            Text(
                                text = state.apellidoError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                OutlinedTextField(
                    value = state.usuarioEditado?.phoneNumber.toString(),
                    onValueChange = {onEvent(SettingsUiEvent.OnPhoneNumberChanged(it)) },
                    label = { Text("Número de teléfono") },
                    placeholder = { Text("8095664123") },
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = state.phoneNumberError != null,
                    supportingText = {
                        if (state.phoneNumberError != null){
                            Text(
                                text = state.phoneNumberError,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {onEvent(SettingsUiEvent.SubmitUsuario)},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Guardar Cambios",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )

}

@Preview
@Composable
fun previewSettingsContent(){
    InfalibleRealEstateTheme() {
        SettingsContent(
            onNavigateToLogin = {},
            onEvent = {},
            state = SettingsUiState(
                usuario = Usuario(
                    id = "1233",
                    nombre = "Ana",
                    apellido = "García",
                    email = "danielito456@gmail.com",
                    userName = "danielito456@gmail.com",
                    phoneNumber = "8095664123",
                    estadoUsuario = "Activo",
                    rol = "22"
                )
            ),
            snack = SnackbarHostState()
        )

    }
}

@Preview(showBackground = true)
@Composable
fun previewEditDialog(){
    InfalibleRealEstateTheme() {
        editDialog(
            onEvent = {},
            state = SettingsUiState()
        )
    }
}
