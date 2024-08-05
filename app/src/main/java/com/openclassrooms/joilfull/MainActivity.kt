package com.openclassrooms.joilfull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.NavGraph

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // permet à l'application de s'étendre sur toute la surface de l'écran, y compris sous les barres de statut et de navigation.

        // Info : Attention : Android studio peut se mettre à bugguer et exécuter plusieurs fois setContent
        // https://stackoverflow.com/questions/72301445/why-is-setcontent-being-called-twice
        setContent {

            // On appelle le NavController
            val navController = rememberNavController()
            NavGraph(
                navController = navController
            )


        }
    }

}

