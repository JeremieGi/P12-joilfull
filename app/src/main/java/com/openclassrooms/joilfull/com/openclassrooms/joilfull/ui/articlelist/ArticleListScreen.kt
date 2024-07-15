package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme


/**
 * Ecran principal qui gère :
 * - l'attente du chargement
 * - l'affichage des articles
 * - l'affichage d'une pop-up d'erreur
 */

@Composable
fun articleListScreen(
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier,
    viewModel : ArticleListViewModel = viewModel()
) {

    // lorsque la valeur uiState est modifiée,
    // la recomposition a lieu pour les composables utilisant la valeur uiState.
    val uiState by viewModel.uiState.collectAsState()

    // En fonction de l'état du viewModel
    when (uiState) {

        // Chargement
        is ArticleListUIState.IsLoading -> {
            LoadingComposable(modifier)
            viewModel.loadArticlesList()
        }

        // Récupération des données avec succès
        is ArticleListUIState.Success -> {


            val listCategoryAndArticles = (uiState as ArticleListUIState.Success).categoryAndArticles
            ArticleListComposable(
                modifier=modifier,
                listCategoryAndArticles = listCategoryAndArticles
            )

            // En mode tablette
            if (windowSize.widthSizeClass == WindowWidthSizeClass.Expanded) {
                // Selected item TODO
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

// TODO : Cette preview ne marche pas
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(
    showBackground = true
)
@Composable
fun articleListScreenPreview() {

    JoilfullTheme {
        articleListScreen(
            windowSize = WindowSizeClass.calculateFromSize(DpSize(411.dp, 891.dp))
        )
    }
}
