package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import android.app.Activity
import android.util.Log
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat

// Les éléments cliquables et interactifs font au moins 48 dp. Cela respecte les directives d'accessibilité de Material Design.
val CTE_MIN_SIZE = 48.dp

fun bDisplayItemOnRight( windowSize: WindowSizeClass? ) : Boolean {

    val bModeTablet : Boolean

    if (windowSize==null){
        bModeTablet = false
    }
    else{

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

    }

    return bModeTablet

}

// Affiche le prix avec deux décimales
fun sDisplayPrice(dPrice : Double) : String {

    val decimalFormat = DecimalFormat("#.##")
    var sPrice = decimalFormat.format(dPrice)

    // Si il y a une virgule
    if  (sPrice.contains(",")) {
        // On la remplace par € : Ex 69,99 => 69€99
        sPrice = sPrice.replace(",","€")
    }
    else{
        // On ajoute € en fin : Ex 69 => 69€
        sPrice += "€"
    }

    return sPrice

}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun getWindowsSize() : WindowSizeClass? {

    val context = LocalContext.current
    val activity = context as? Activity

    var windowSizeClass : WindowSizeClass? = null
    activity?.let {
        windowSizeClass = calculateWindowSizeClass(activity)
    }

    return windowSizeClass

}

fun logCompose(sLogP : String){
    Log.d("**Compose**",sLogP)
}
