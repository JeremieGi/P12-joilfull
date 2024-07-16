package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleScreen
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist.articleListScreen
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme


/**
 * Configurer les Routes de Navigation
 */

@Composable
fun NavGraph(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass
) {

    NavHost(
        navController = navController,
        startDestination = "articleList"    // Point d'entrée de l'application
    ) {

        // Fenêtre de la liste des articles
        composable("articleList") { backStackEntry ->

            StructureComposable(

            ){ modifier ->
                articleListScreen(
                    navController = navController,
                    windowSize = windowSizeClass,
                    modifier = modifier
                )
            }

        }


        // Fenêtre d'un article
        composable("articleItem/{articleId}") { backStackEntry ->

            StructureComposable(

            ){ modifier ->

                val articleId = backStackEntry.arguments?.getString("articleId")
                ArticleScreen(
                    articleId = articleId?.toInt() ?: -1,
                    navController = navController,
                    modifier = modifier
                )

            }

        }

    }
}


// Structuration de toutes les fenêtres de l'application
// avec application du thème et padding pour ne pas se superposer à la bar Android
@Composable
fun StructureComposable(
    functionComposableParam : @Composable (modifier: Modifier) -> Unit
){

    JoilfullTheme {
        Scaffold(

            content = { innerPadding -> // 24dp top and bottom

                functionComposableParam(
                    modifier = Modifier.padding(innerPadding)
                )
            }

        )
    }

}
