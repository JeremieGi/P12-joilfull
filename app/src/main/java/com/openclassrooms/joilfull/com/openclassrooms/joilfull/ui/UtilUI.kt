package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

fun bTablet( windowSize: WindowSizeClass ) : Boolean {

    val bModeTablet : Boolean

    when (windowSize.widthSizeClass) {

        WindowWidthSizeClass.Expanded/*, WindowWidthSizeClass.Medium*/ -> {
            // Affiche la fiche article dans la même fenêtre uniquement pour un tablette en mode portrait
            // Si j'ajoute Medium => Ca affiche la fiche Article aussi en tablette mode paysage, mais aussi en téléphone mode paysage
            bModeTablet = true
        }

        else -> {
            bModeTablet = false
        }
    }

    return bModeTablet

}