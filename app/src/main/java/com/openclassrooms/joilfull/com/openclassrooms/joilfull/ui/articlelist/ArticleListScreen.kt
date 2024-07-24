package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.openclassrooms.joilfull.Links
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleItemContent
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.bTablet
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.getWindowsSize
import com.openclassrooms.joilfull.model.Article


/**
 * Ecran principal qui gère :
 * - l'attente du chargement
 * - l'affichage des articles
 * - l'affichage d'une pop-up d'erreur
 */

@Composable
fun ArticleListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModelList : ArticleListViewModel = hiltViewModel() // Par défaut, Hilt utilise la portée @ActivityRetainedScoped pour les ViewModels, ce qui signifie que le même ViewModel est partagé pour toute la durée de vie de l'activité.
) {

    // Recharger les articles quand l'écran est visible
    LaunchedEffect(Unit) {
        viewModelList.loadArticlesList()
    }

    // lorsque la valeur uiState est modifiée,
    // la recomposition a lieu pour les composables utilisant la valeur uiState.
    val uiStateList by viewModelList.uiState.collectAsState()

    val windowSize = getWindowsSize()

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
                val uiStateArticle = (uiStateList as ArticleListUIState.Success).uiStateArticleSelect


                val onArticleClickP : (Article) -> Unit

                if (bTablet(windowSize)){

                    // Tablette =>
                    //  - charge l'article depuis le viewModel
                    //  - va recomposer ce composant avec l'article sélectionné
                    onArticleClickP = { article ->
                        viewModelList.loadArticleByID(article.nIDArticle)
                    }

                }
                else{
                    // Phone => useNavHost
                    onArticleClickP = { article ->
                        // TODO Denis : pourquoi ArticleListScreen est recomposé après cet appel ?
                        navController.navigate("${Links.CTE_ROUTE}/${article.nIDArticle}")

                    }
                }

                Row{

                    ArticleListComposable(
                        modifier=Modifier
                            .weight(2f),
                        onArticleClickP = onArticleClickP,
                        listCategoryAndArticles = listCategoryAndArticles
                    )


                    // En mode tablette
                    // Et un article est sélectionné

                    if (bTablet(windowSize)) {

                        if (uiStateArticle==null) {
                            // Aucun article n'est sélectionné
                            // Rien ne s'affiche à droite
                        }
                        else{

                            ArticleItemContent(
                                modifier = Modifier
                                    .weight(1f),
                                uiState = uiStateArticle,
                                nIDRessourceAvatarP = viewModelList.getCurrentUserAvatar(),
                                onClickBackP = null, // Pas de backstack en mode tablette
                                onClickSendNoteP = viewModelList::sendNoteAndComment,
                                onClickLikeP = viewModelList::setLike,
                                unselectArticle = viewModelList::unselectArticle
                            )

                        }

                    }
                    else{
                        // Ouverture dans une autre fenêtre via navController.navigate

                    }


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