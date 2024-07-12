package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme


@Composable
fun ArticleListScreen(
    modifier: Modifier = Modifier,
    viewModel : ArticleListViewModel = viewModel()
) {

    // lorsque la valeur uiState est modifiée,
    // la recomposition a lieu pour les composables utilisant la valeur uiState.
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {

        is ArticleListUIState.isLoading -> {
            Text(text = stringResource(R.string.chargement))
            viewModel.loadArticlesList()
        }

        is ArticleListUIState.Success -> {

            val articles = (uiState as ArticleListUIState.Success).articles

            Column {
                Text(
                    text = "Liste des ${articles.size} articles à implémenter",
                    modifier = modifier
                )

            }
        }
        is ArticleListUIState.Error -> {
            val error = (uiState as ArticleListUIState.Error).exception.message ?: "Unknown error"
            ErrorDialog(sMessage = error)
        }
    }





}

@Preview(showBackground = true)
@Composable
fun ArticleListScreenPreview() {
    JoilfullTheme {
        ArticleListScreen()
    }
}

