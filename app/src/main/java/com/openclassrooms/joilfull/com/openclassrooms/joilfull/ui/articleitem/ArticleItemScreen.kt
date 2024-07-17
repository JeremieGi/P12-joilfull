package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
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
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.sDisplayPrice
import com.openclassrooms.joilfull.ui.theme.colorStar


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
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .clickable {
                onArticleClickP(article)
            }
            .fillMaxSize()

        //color = MaterialTheme.colorScheme.background
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                //.height(IntrinsicSize.Min)
        ) {

            GlideImage(
                model = article.sURLPicture,
                contentDescription = article.sDescriptionPicture,
                contentScale = ContentScale.Crop, // Échelle de contenu
                modifier = modifier
                    .weight(1f) // Prend tout l'espace disponible en laissant afficher les colonnes dessous
                    .fillMaxSize()
                    /*.border(
                        // Juste pour la preview (A commenter)
                        width = 10.dp,
                        color = Color.Black,
                    )*/
                    .graphicsLayer {
                        shape = RoundedCornerShape(16.dp) // Coins arrondis
                    },
            )

            // Adaptation des polices si on est dans la fenêtre de détail ou de liste
            val typo : TextStyle
            if (bModeDetail){
                typo = MaterialTheme.typography.titleLarge
            }
            else{
                typo = MaterialTheme.typography.titleSmall
            }


            Row(
                modifier = Modifier
                    .wrapContentHeight() // Adapte la hauteur de la Row à son contenu

            ){
                Text(
                    text = article.sName,
                    modifier = Modifier
                        .weight(1f), // Prend tout l'espace disponible en laissant afficher les lignes à droite
                    style =  typo
                )

                // Affichage de la note moyenne
                Row (
                    modifier = Modifier
                        .wrapContentHeight()
                ){

                    Icon(
                        imageVector = Icons.Filled.Star,
                        modifier = Modifier
                            .wrapContentHeight(),
                        contentDescription = stringResource(R.string.etoile),
                        tint = colorStar // TODO : J'ai 2 fichiers de couleurs Color.kt et colors.xml : lequel utiliser ?
                    )
                    Text(
                        text = "X.X", // TODO : Moyenne des notes
                        modifier = Modifier
                            .wrapContentWidth()
                            /*.align(Alignment.CenterHorizontally)*/,
                        style =  typo
                    )

                }


            }

            Row(
                modifier = Modifier
                    .wrapContentHeight() // Adapte la hauteur de la Row à son contenu
            ){
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = sDisplayPrice(article.dPrice), // Définit le format à deux décimales
                    style =  typo
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = sDisplayPrice(article.dOriginalPrice), // Définit le format à deux décimales
                    style =  typo,
                    textDecoration = TextDecoration.LineThrough,
                    textAlign = TextAlign.Right,
                )
            }


            

        }

    }

}







@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES/*, showBackground = true*/)
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
