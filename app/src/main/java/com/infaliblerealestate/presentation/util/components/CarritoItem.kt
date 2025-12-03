package com.infaliblerealestate.presentation.util.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.infaliblerealestate.dominio.model.Propiedades
import com.infaliblerealestate.dominio.model.PropiedadesDetalle
import com.infaliblerealestate.ui.theme.InfalibleRealEstateTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarritoItem(
    propiedad: Propiedades,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onRemoveFromCart: () -> Unit = {}
){
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{onClick()},
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            if (propiedad.imagenes.isEmpty()) {
                Box(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay imágenes disponibles",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }else{
                AsyncImage(
                    model = propiedad.imagenes.firstOrNull()?.urlImagen,
                    contentDescription = "Imagen de la propiedad",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.Top)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${propiedad.ciudad}, ${propiedad.estadoProvincia}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Surface(
                        onClick = { onRemoveFromCart() },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
                        modifier = Modifier
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar del carrito",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(20.dp)
                        )
                    }
                }

                Text(
                    text = propiedad.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    Text(
                        text = "${if (propiedad.moneda == "Peso") "RD$" else "USD$"} ${
                            String.format(
                                "%,.0f",
                                propiedad.precio
                            )
                        }",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarritoItemPreview(){
    InfalibleRealEstateTheme() {
        CarritoItem(
            propiedad = Propiedades(
                propiedadId = 92,
                titulo = "Casa en el centro de la ciudad",
                ciudad = "Ciudad de México",
                estadoProvincia = "Ciudad de México",
                precio = 1000000.0,
                moneda = "Peso",
                administradorId =  "2ddd",
                tipoTransaccion = "Venta",
                categoriaId = 1,
                fechaActualizacion = "",
                fechaPublicacion = "",
                estadoPropiedadId = 3,
                imagenes = emptyList(),
                detalle = PropiedadesDetalle(
                    propiedadId = 92,
                    habitaciones = 3,
                    banos = 2.0,
                    parqueo = 1,
                    metrosCuadrados = 100.0,
                    descripcion = "Una hermosa casa en el centro de la ciudad"
                )
            ),
        )
    }


}