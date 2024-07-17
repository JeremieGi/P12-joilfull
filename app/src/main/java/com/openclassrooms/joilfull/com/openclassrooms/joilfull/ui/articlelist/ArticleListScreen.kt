package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleItemComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.bTablet
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import androidx.hilt.navigation.compose.hiltViewModel
import com.openclassrooms.joilfull.model.Article


/**
 * Ecran principal qui gère :
 * - l'attente du chargement
 * - l'affichage des articles
 * - l'affichage d'une pop-up d'erreur
 */

@Composable
fun ArticleListScreen(
    windowSize: WindowSizeClass,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel : ArticleListViewModel = hiltViewModel(), // Par défaut, Hilt utilise la portée @ActivityRetainedScoped pour les ViewModels, ce qui signifie que le même ViewModel est partagé pour toute la durée de vie de l'activité.

) {

    // lorsque la valeur uiState est modifiée,
    // la recomposition a lieu pour les composables utilisant la valeur uiState.
    val uiState by viewModel.uiState.collectAsState()

    // En fonction de l'état du viewModel
    when (uiState) {

        // Chargement
        is ArticleListUIState.IsLoading -> {
            LoadingComposable(modifier)
        }

        // Récupération des données avec succès
        is ArticleListUIState.Success -> {

            Row(
                modifier=modifier,
            ){

                val listCategoryAndArticles = (uiState as ArticleListUIState.Success).categoryAndArticles

                val selectedArticle = (uiState as ArticleListUIState.Success).selectedArticle

                val onArticleClickP : (Article) -> Unit
                if (bTablet(windowSize)){
                    // Tablette => affiche l'article dans le même composant
                    onArticleClickP = viewModel::selectArticle

                }
                else{
                    // Phone => useNavHost
                    onArticleClickP = { article ->
                        // TODO : pourquoi ArticleListScreen est recomposé après cet appel ?
                        navController.navigate("articleItem/${article.nIDArticle}")
                    }
                }

                ArticleListComposable(
                    modifier=modifier,
                    onArticleClickP = onArticleClickP,
                    listCategoryAndArticles = listCategoryAndArticles
                )

                // En mode tablette
                // Et un article est sélectionné

                if (selectedArticle != null){

                    if (bTablet(windowSize)) {

                        // Affichage de l'article dans l'écran
                        ArticleItemComposable(
                            modifier=modifier,
                            article = selectedArticle,
                            onArticleClickP = {}
                        )

                    }
                    /*
                    else{
                        // Ouverture dans une autre fenêtre
                        navController.navigate("articleItem/${selectedArticle.nIDArticle}")
                    }
                    */
                }


            }


        }

        // Exception
        is ArticleListUIState.Error -> {

            val error = (uiState as ArticleListUIState.Error).exception.message ?: "Unknown error"
            ErrorComposable(
                modifier=modifier,
                sMessage = error,
                onClickRetryP = { viewModel.loadArticlesList() }
            )


        }
    }



}
/*
// TODO : Cette preview ne marche pas : Failed to instantiate a ViewModel
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    showBackground = true
)
@Composable
fun ArticleListScreenPreview() {

    val navController = rememberNavController()

    JoilfullTheme {
        ArticleListScreen(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp)),
            navController = navController
        )
    }
}
*/