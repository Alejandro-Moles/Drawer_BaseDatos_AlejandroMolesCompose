package com.example.milistaanimes.Controlador

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class ItemsNav(val ruta : String, val titulo : String, val icono : ImageVector){

    object Home: ItemsNav("Home", "Home", Icons.Filled.Home)
    object Insertar: ItemsNav("Insertar", "Insertar", Icons.Filled.Add)
    object Favoritos: ItemsNav("Favoritos", "Favoritos", Icons.Filled.Favorite)

}
