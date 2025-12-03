package com.infaliblerealestate.presentation.util.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.infaliblerealestate.presentation.carrito.CarritoScreen
import com.infaliblerealestate.presentation.catalogo.CatalogoScreen
import com.infaliblerealestate.presentation.home.HomeScreen
import com.infaliblerealestate.presentation.login.LoginScreen
import com.infaliblerealestate.presentation.settings.SettingsScreen
import com.infaliblerealestate.presentation.upsertPropiedad.UpsertPropiedadScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ){
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToCatalogo = { categoria ->
                    navController.navigate(
                        Screen.Catalogo.createRoute(Uri.encode(categoria))
                    ) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToUpsertPropiedad = { usuarioId, propiedadId, isAdmin ->
                    navController.navigate(
                        Screen.UpsertPropiedad.createRoute(usuarioId, propiedadId, isAdmin)
                    )
                }
            )
        }

        composable(
            route = Screen.Settings.route,
            arguments = listOf(
                navArgument(Screen.Settings.ARG) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(Screen.Settings.ARG)
            SettingsScreen(
                usuarioId = id,
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Carrito.route,
            arguments = listOf(
                navArgument(Screen.Carrito.ARG) { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(Screen.Carrito.ARG)
            CarritoScreen(usuarioId = id)
        }

        composable(
            route = Screen.Catalogo.route,
            arguments = listOf(
                navArgument(Screen.Catalogo.CATEGORIA_ARG) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val categoria = backStackEntry.arguments?.getString(Screen.Catalogo.CATEGORIA_ARG)
            CatalogoScreen(
                categoriaInicial = categoria,
                onNavigateToUpsertPropiedad = { usuarioId, propiedadId, isAdmin ->
                    navController.navigate(
                        Screen.UpsertPropiedad.createRoute(usuarioId, propiedadId, isAdmin)
                    )
                }
            )
        }

        composable(
            route = Screen.UpsertPropiedad.route,
            arguments = listOf(
                navArgument(Screen.UpsertPropiedad.USUARIO_ID_ARG) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(Screen.UpsertPropiedad.PROPIEDAD_ID_ARG) {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("isAdmin") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString(Screen.UpsertPropiedad.USUARIO_ID_ARG)
            val propiedadId = backStackEntry.arguments?.getInt(Screen.UpsertPropiedad.PROPIEDAD_ID_ARG) ?.takeIf { it != -1 }
            val isAdmin = backStackEntry.arguments?.getBoolean("isAdmin") ?: false
            UpsertPropiedadScreen(
                usuarioId = usuarioId,
                propiedadId = propiedadId,
                isAdmin = isAdmin,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.createRoute()) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
