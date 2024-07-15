package com.openclassrooms.joilfull

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist.articleListScreen
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val windowSizeClass = calculateWindowSizeClass(this)

            JoilfullTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Appelle l'écran principal
                    articleListScreen(
                        windowSize = windowSizeClass,
                        modifier = Modifier.padding(innerPadding)
                    )

                }
            }
        }
    }

}

