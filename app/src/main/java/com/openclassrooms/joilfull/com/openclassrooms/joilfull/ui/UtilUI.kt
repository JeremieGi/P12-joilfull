package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

fun bTablet( windowSize: WindowSizeClass ) : Boolean {

    val bModeTablet : Boolean

    when (windowSize.widthSizeClass) {

        WindowWidthSizeClass.Expanded -> {
            bModeTablet = true
        }

        else -> {
            bModeTablet = false
        }
    }

    return bModeTablet

}