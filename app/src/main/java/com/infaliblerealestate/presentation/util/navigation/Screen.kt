package com.infaliblerealestate.presentation.util.navigation

sealed class Screen(val route: String){
    data object Home: Screen("home_screen"){
        fun createRoute() = "home_screen"
    }
    data object Settings: Screen("settings_screen/{id}"){
        const val ARG = "id"
        fun createRoute(id: String) = "settings_screen/$id"
    }

    data object Carrito: Screen("carrito_screen/{id}"){
        const val ARG = "id"
        fun createRoute(id: String) = "carrito_screen/$id"
    }

    data object Catalogo: Screen("catalogo_screen?categoria={categoria}"){
        const val CATEGORIA_ARG = "categoria"
        fun createRoute(categoria: String? = null) =
            if (categoria != null) "catalogo_screen?categoria=$categoria"
            else "catalogo_screen"
    }

    data object UpsertPropiedad: Screen("upsert_propiedad_screen?usuarioId={usuarioId}&propiedadId={propiedadId}") {
        const val USUARIO_ID_ARG = "usuarioId"
        const val PROPIEDAD_ID_ARG = "propiedadId"
        fun createRoute(usuarioId: String?, propiedadId: Int? = null, isAdmin: Boolean = false) =
            if (propiedadId != null)
                "upsert_propiedad_screen?usuarioId=$usuarioId&propiedadId=$propiedadId"
            else
                "upsert_propiedad_screen?usuarioId=$usuarioId&isAdmin=$isAdmin"
    }

    data object Login: Screen("login_screen")
}