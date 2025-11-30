package com.infaliblerealestate.presentation.util.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AppBottomBar(
    navController: NavController,
    currentRoute: String?,
    usuarioId: String
) {
    NavigationBar(
        modifier = Modifier
            .height(70.dp)
            .shadow(8.dp, RoundedCornerShape(50.dp))
            .clip(RoundedCornerShape(50.dp)),
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        BottomNavItem.items.forEach { item ->
            val baseRoute = item.screen.route.substringBefore("/")
            val selected = currentRoute?.startsWith(baseRoute) == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        val route = when (item.screen) {
                            is Screen.Home -> Screen.Home.createRoute()
                            is Screen.Settings -> Screen.Settings.createRoute(usuarioId)
                            is Screen.Carrito -> Screen.Carrito.createRoute(usuarioId)
                            is Screen.Catalogo -> Screen.Catalogo.createRoute()
                            else -> return@NavigationBarItem
                        }
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clip(RoundedCornerShape(0.dp))
            )
        }
    }
}

@Preview
@Composable
fun AppBottomBarPreview() {
    val navController = NavController(LocalContext.current)
    AppBottomBar(
        navController = navController,
        currentRoute = "home_screen/1",
        usuarioId = "PEOKIJRVBHDJSO"
    )

}
