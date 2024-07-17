package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.sDisplayPrice
import java.text.DecimalFormat


// Ce point d'entrée est utilisé uniquement pour les petits écrans
@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    articleId: Int,
    viewModel: ArticleViewModel = hiltViewModel(), // View Model depuis le graph de navigation

) {

    // Trigger loading article details when articleId changes
    // Premier lancement
    LaunchedEffect(articleId) {
        viewModel.loadArticleByID(articleId)
    }

    val uiState by viewModel.uiState.collectAsState()

    // En fonction de l'état du viewModel
    when (uiState) {

        // Chargement
        is ArticleUIState.IsLoading -> {
            LoadingComposable(modifier)
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
                    bModeDetail = true,
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
    bModeDetail : Boolean,  // Vrai => Mode détails, faux => Mode Item
    onArticleClickP : (Article) -> Unit,
    modifier: Modifier = Modifier
) {
    

    Card(
        modifier = Modifier
            //.padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable {
                onArticleClickP(article)
            }
            .fillMaxSize()

        //color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = modifier.fillMaxSize(),
        ) {

            val nHauteurImage : Int
            if (bModeDetail){
                nHauteurImage = 430
            }
            else{
                nHauteurImage = 198
            }

            GlideImage(
                model = article.sURLPicture,
                contentDescription = article.sDescriptionPicture,
                modifier = modifier
                    .requiredHeight(nHauteurImage.dp)
                    .fillMaxWidth()
                    .border( // Juste pour la preview (A commenter)
                        width = 10.dp,
                        color = Color.Black,
                    )
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    text = article.sName,
                    modifier = Modifier.weight(8f),
                    style =  MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "TODO : Notation",
                    modifier = Modifier.weight(2f),
                    style =  MaterialTheme.typography.titleLarge
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ){
                Text(
                    modifier = Modifier.weight(1f),
                    text = sDisplayPrice(article.dPrice), // Définit le format à deux décimales
                    style =  MaterialTheme.typography.titleLarge
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = sDisplayPrice(article.dOriginalPrice), // Définit le format à deux décimales
                    style =  MaterialTheme.typography.titleLarge,
                    textDecoration = TextDecoration.LineThrough,
                    textAlign = TextAlign.Right,
                )
            }

        }

    }

}







@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ArticleItemComposablePreview() {

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
            bModeDetail = true,
            onArticleClickP = {}
        )
    }
}
