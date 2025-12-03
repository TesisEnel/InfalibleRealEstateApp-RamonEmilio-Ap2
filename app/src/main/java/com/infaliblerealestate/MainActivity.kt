package com.infaliblerealestate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.infaliblerealestate.presentation.util.navigation.AppBottomBar
import com.infaliblerealestate.presentation.util.navigation.AppNavHost
import com.infaliblerealestate.ui.theme.InfalibleRealEstateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InfalibleRealEstateTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = when {
                    currentRoute?.startsWith("home_screen") == true -> true
                    currentRoute?.startsWith("settings_screen") == true -> true
                    currentRoute?.startsWith("carrito_screen") == true -> true
                    currentRoute?.startsWith("catalogo_screen") == true -> true
                    else -> false
                }

                val usuarioId = remember(navBackStackEntry) {
                    navController.currentBackStack.value
                        .mapNotNull { it.arguments?.getString("id") }
                        .firstOrNull() ?: ""
                }

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavHost(navController = navController, modifier = Modifier)
                    }

                    if (showBottomBar) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(horizontal = 16.dp, vertical = 16.dp)
                        ) {
                            AppBottomBar(
                                navController = navController,
                                currentRoute = currentRoute,
                                usuarioId = usuarioId
                            )
                        }
                    }
                }
            }
        }
    }
}

