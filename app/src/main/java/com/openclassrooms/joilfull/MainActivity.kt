package com.openclassrooms.joilfull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.MainScreen
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JoilfullTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Appelle l'écran principal
                    MainScreen(
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }

}

