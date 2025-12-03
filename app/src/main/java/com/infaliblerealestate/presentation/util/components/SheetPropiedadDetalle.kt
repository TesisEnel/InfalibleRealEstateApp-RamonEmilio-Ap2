package com.infaliblerealestate.presentation.util.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.Garage
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infaliblerealestate.dominio.model.ImagenPropiedad
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.model.PropiedadesDetalle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetPropiedadDetalle(
    propiedad: Propiedades?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val screenWidth = configuration.screenWidthDp.dp

    val imageHeight = when {
        screenWidth < 360.dp -> 180.dp
        screenWidth < 600.dp -> 250.dp
        screenWidth < 840.dp -> 320.dp
        else -> 400.dp
    }

    val horizontalPadding = when {
        screenWidth < 600.dp -> 16.dp
        screenWidth < 840.dp -> 24.dp
        else -> 32.dp
    }

    propiedad?.let { propiedadDetalle ->
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            modifier = modifier,
            dragHandle = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier
                            .width(32.dp)
                            .height(4.dp),
                        shape = RoundedCornerShape(2.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    ) {}
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding)
                    .navigationBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
            ) {

                HorizontalCenteredHeroCarousel(
                    images = propiedadDetalle.imagenes,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = propiedadDetalle.titulo,
                    style = if (screenWidth < 600.dp) {
                        MaterialTheme.typography.headlineSmall
                    } else {
                        MaterialTheme.typography.headlineMedium
                    },
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "${propiedadDetalle.ciudad}, ${propiedadDetalle.estadoProvincia}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${if (propiedadDetalle.moneda == "Peso") "RD$" else "USD$"} ${String.format("%,.2f", propiedadDetalle.precio)}",
                    style = if (screenWidth < 600.dp) {
                        MaterialTheme.typography.headlineMedium
                    } else {
                        MaterialTheme.typography.headlineLarge
                    },
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = if (screenWidth < 360.dp) 2 else if (screenWidth < 600.dp) 3 else 4
                ) {
                    if (propiedadDetalle.detalle.habitaciones > 0) {
                        PropiedadChip(
                            icon = Icons.Default.Bed,
                            text = "${propiedadDetalle.detalle.habitaciones} Hab."
                        )
                    }
                    if (propiedadDetalle.detalle.banos > 0) {
                        PropiedadChip(
                            icon = Icons.Default.Bathtub,
                            text = "${String.format("%,.1f", propiedadDetalle.detalle.banos)} Baños"
                        )
                    }
                    if (propiedadDetalle.detalle.parqueo > 0) {
                        PropiedadChip(
                            icon = Icons.Default.Garage,
                            text = "${propiedadDetalle.detalle.parqueo} Parqueo${if (propiedadDetalle.detalle.parqueo > 1) "s" else ""}"
                        )
                    }
                    if (propiedadDetalle.detalle.metrosCuadrados > 0) {
                        PropiedadChip(
                            icon = Icons.Default.SquareFoot,
                            text = "${String.format("%,.1f", propiedadDetalle.detalle.metrosCuadrados)} m²"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripción:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = propiedadDetalle.detalle.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(if (isLandscape) 16.dp else 32.dp))
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SheetPropiedadDetallePreview() {
    val detalleDemo = PropiedadesDetalle(
        propiedadId = 1,
        descripcion = "Amplio apartamento moderno en el centro de la ciudad, cerca de todo.",
        habitaciones = 3,
        banos = 2.0,
        parqueo = 2,
        metrosCuadrados = 120.0
    )

    val imagenesDemo = listOf(
        ImagenPropiedad(
            imagenId = 1,
            propiedadId = 1,
            urlImagen = "https://via.placeholder.com/800x600",
            orden = 1
        ),
        ImagenPropiedad(
            imagenId = 2,
            propiedadId = 1,
            urlImagen = "https://via.placeholder.com/800x600?2",
            orden = 2
        )
    )

    val propiedadDemo = Propiedades(
        propiedadId = 1,
        administradorId = "admin-001",
        titulo = "Apartamento moderno en el centro",
        precio = 1_250_000.0,
        moneda = "Peso",
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

    SheetPropiedadDetalle(
        propiedad = propiedadDemo,
        onDismiss = {}
    )
}
