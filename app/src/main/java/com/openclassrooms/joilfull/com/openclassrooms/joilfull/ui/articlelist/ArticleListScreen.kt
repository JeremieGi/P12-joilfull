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
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleItemContent
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleScreen
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleUIState
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleViewModel
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
    viewModelList : ArticleListViewModel = hiltViewModel(), // Par défaut, Hilt utilise la portée @ActivityRetainedScoped pour les ViewModels, ce qui signifie que le même ViewModel est partagé pour toute la durée de vie de l'activité.
    viewModelArticle : ArticleViewModel = hiltViewModel()
) {

    // lorsque la valeur uiState est modifiée,
    // la recomposition a lieu pour les composables utilisant la valeur uiState.
    val uiStateList by viewModelList.uiState.collectAsState()




    // En fonction de l'état du viewModel
    when (uiStateList) {

        // Chargement
        is ArticleListUIState.IsLoading -> {
            LoadingComposable(modifier)
        }

        // Récupération des données avec succès
        is ArticleListUIState.Success -> {

            Row(
                modifier=modifier,
            ){

                val listCategoryAndArticles = (uiStateList as ArticleListUIState.Success).categoryAndArticles

                val uiStateArticle by viewModelArticle.uiState.collectAsState()

                val onArticleClickP : (Article) -> Unit

                if (bTablet(windowSize)){

                    // Tablette =>
                    //  - charge l'article depuis le viewModel
                    //  - va recomposer ce composant avec l'article sélectionné
                    onArticleClickP = { article ->
                        viewModelArticle.loadArticleByID(article.nIDArticle)
                    }

                }
                else{
                    // Phone => useNavHost
                    onArticleClickP = { article ->
                        // TODO Denis : pourquoi ArticleListScreen est recomposé après cet appel ?
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

                if (bTablet(windowSize)) {

                    if (uiStateArticle is ArticleUIState.NoSelectedArticle) {
                        // Aucun article n'est sélectionné
                        // Rien ne s'affiche
                    }
                    else{

                        ArticleItemContent(
                            modifier = modifier,
                            uiState = uiStateArticle
                        )
                    }

                }
                else{
                    // Ouverture dans une autre fenêtre via navController.navigate
                }

            }


        }

        // Exception
        is ArticleListUIState.Error -> {

            val error = (uiStateList as ArticleListUIState.Error).exception.message ?: "Unknown error"
            ErrorComposable(
                modifier=modifier,
                sMessage = error,
                onClickRetryP = { viewModelList.loadArticlesList() }
            )


        }
    }



}

/*
// TODO Denis 2  : Cette preview ne marche pas : Failed to instantiate a ViewModel
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