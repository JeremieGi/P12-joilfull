package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme

/**
 * Ecran principal qui gère :
 * - l'attente du chargement
 * - l'afficahge des articles
 * - l'affichage d'une pop-up d'erreur
 */

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel : MainViewModel = viewModel()
) {

    // lorsque la valeur uiState est modifiée,
    // la recomposition a lieu pour les composables utilisant la valeur uiState.
    val uiState by viewModel.uiState.collectAsState()

    // En fonction de l'état du viewModel
    when (uiState) {

        // Chargement
        is ArticleListUIState.IsLoading -> {
            LoadingScreen(modifier)
            viewModel.loadArticlesList()
        }

        // Récupération des données avec succès
        is ArticleListUIState.Success -> {

            val articles = (uiState as ArticleListUIState.Success).articles
            ArticleListScreen(
                modifier=modifier,
                listArticles = articles)

        }

        // Exception
        is ArticleListUIState.Error -> {

            val error = (uiState as ArticleListUIState.Error).exception.message ?: "Unknown error"
            ErrorDialog(
                modifier=modifier,
                sMessage = error,
                onClickRetryP = { viewModel.loadArticlesList() }
            )


        }
    }

}

// TODO : Cette preview ne marche pas
@Preview(
    showBackground = true
)
@Composable
fun MainScreenPreview() {

    JoilfullTheme {
        MainScreen()
    }
}
