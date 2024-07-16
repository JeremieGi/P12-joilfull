package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import com.bumptech.glide.integration.compose.GlideImage
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable



@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    articleId: Int,
    viewModel: ArticleViewModel = hiltViewModel(), // View Model depuis le graph de navigation

) {


    val uiState by viewModel.uiState.collectAsState()

    // En fonction de l'état du viewModel
    when (uiState) {

        // Chargement
        is ArticleUIState.IsLoading -> {
            LoadingComposable(modifier)
            viewModel.loadArticleByID(articleId)
        }

        // Récupération des données avec succès
        is ArticleUIState.Success -> {

            val article = (uiState as ArticleUIState.Success).article

            Column(
                modifier = modifier
            ){

                // Bouton visible uniquement en mode téléphone
                Button(onClick = {
                    navController.popBackStack()
                }) {
                    Text("Back")
                }

                ArticleItemComposable(
                    article = article,
                    onArticleClickP = {} // On Click neutralisé
                )
            }

        }

        // Exception
        is ArticleUIState.Error -> {

            val error = (uiState as ArticleUIState.Error).exception.message ?: "Unknown error"
            ErrorComposable(
                modifier=modifier,
                sMessage = error,
                onClickRetryP = { viewModel.loadArticleByID(articleId) }
            )


        }

    }


}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleItemComposable(
    article : Article,
    onArticleClickP : (Article) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            //.padding(horizontal = 16.dp, vertical = 4.dp)
            //.size(250.dp) // Définir une taille fixe pour toutes les cartes
            .clickable {
                onArticleClickP(article)
            }

        //color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = modifier.fillMaxSize(),
        ) {

            GlideImage(
                model = article.sURLPicture,
                contentDescription = article.sDescriptionPicture,
                modifier = modifier
                    .requiredHeight(100.dp)
                    //.background(Color.Red)
                    //.fillMaxSize()
                    .border(
                        width = 10.dp,
                        color = Color.Black,
                    )
            )

            Text(text = article.sName)
        }

    }

}







@Preview(
    showBackground = true
)
@Composable
fun ArticleScreenPreview() {

    val article = Article(
            nIDArticle = 0,
            sURLPicture = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/1.jpg",
            sDescriptionPicture = "Sac à main orange posé sur une poignée de porte",
            sName = "Sac à main orange",
            sCategory = "ACCESSORIES", // Enumération ici : pas trop d'intéret si jamais le WS renvoie une nouvelle catégorie
            nNbLikes = 56,
            dPrice = 69.99,
            dOriginalPrice = 99.00,
            bFavorite = false
    )


    JoilfullTheme {
        ArticleItemComposable(
            article = article,
            onArticleClickP = {}
        )
    }
}
