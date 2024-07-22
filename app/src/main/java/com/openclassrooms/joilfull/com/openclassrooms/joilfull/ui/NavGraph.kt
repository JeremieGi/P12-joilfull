package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.openclassrooms.joilfull.Links
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleScreen
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist.ArticleListScreen
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme


/**
 * Configurer les Routes de Navigation
 */

@Composable
fun NavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = "articleList"    // Point d'entrée de l'application
    ) {

        // Fenêtre de la liste des articles
        composable("articleList") {

            StructureComposable(

            ){ modifier ->
                ArticleListScreen(
                    navController = navController,
                    modifier = modifier
                )
            }

        }


        // Fenêtre d'un article
        composable(

            route = Links.routeWithArgs, // "articleItem/{articleId}"
            deepLinks = Links.deepLinks, // "joiefull://articleItem/{articleId}"
            arguments = Links.arguments

        ) { backStackEntry -> // BackStackEntry ici permet de récupérer les paramètres

            StructureComposable(

            ){ modifier ->

                val articleId = backStackEntry.arguments?.getString(Links.articleIdArg)
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
