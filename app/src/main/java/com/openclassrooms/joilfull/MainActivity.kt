package com.openclassrooms.joilfull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.NavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            // Détermine la taille de la fenêtre
            val windowSizeClass = calculateWindowSizeClass(this)

            // On appelle le NavController
            val navController = rememberNavController()
            NavGraph(
                navController = navController,
                windowSizeClass = windowSizeClass
            )



        }
    }

}

