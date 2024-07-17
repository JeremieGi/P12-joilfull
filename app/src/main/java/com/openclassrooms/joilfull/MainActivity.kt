package com.openclassrooms.joilfull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.NavGraph
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist.ArticleListScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // permet à l'application de s'étendre sur toute la surface de l'écran, y compris sous les barres de statut et de navigation.

        // TODO : Attention : Android studio peut se mettre à bugguer et exécuter plusieurs fois setContent
        // https://stackoverflow.com/questions/72301445/why-is-setcontent-being-called-twice
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

