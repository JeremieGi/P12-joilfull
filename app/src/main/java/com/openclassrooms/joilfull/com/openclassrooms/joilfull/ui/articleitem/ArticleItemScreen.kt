package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.sDisplayPrice
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import com.openclassrooms.joilfull.ui.theme.colorHeart
import com.openclassrooms.joilfull.ui.theme.colorStar


// Ce point d'entrée est utilisé uniquement pour les petits écrans
@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    articleId: Int,
    viewModel: ArticleViewModel = hiltViewModel(), // View Model depuis le graph de navigation

) {

    // Bouton visible uniquement lors de l'appel à ArticleScreen (c'est à dire utilisation de navController)
    Button(onClick = {
        navController.popBackStack()
    }) {
        Text("Back")
    }



    // Trigger loading article details when articleId changes
    // Premier lancement
    LaunchedEffect(articleId) {
        viewModel.loadArticleByID(articleId)
    }

    val uiState by viewModel.uiState.collectAsState()

    val onLoadArticle = { viewModel.loadArticleByID(articleId) }
    ArticleItemContent(
        modifier = modifier,
        uiState = uiState,
        onClickErrorRetryP = onLoadArticle
    )

}

@Composable
fun ArticleItemContent(
    uiState: ArticleUIState,
    modifier: Modifier = Modifier,
    onClickErrorRetryP : () -> Unit
){

    // En fonction de l'état du viewModel
    when (uiState) {

        is ArticleUIState.InitState -> {

        }

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
                onClickRetryP = onClickErrorRetryP
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

    // Adaptation des polices si on est dans la fenêtre de détail ou de liste
    val typo : TextStyle
    if (bModeDetail){
        typo = MaterialTheme.typography.titleLarge
    }
    else{
        typo = MaterialTheme.typography.titleSmall
    }
    

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
            modifier = modifier
                .fillMaxSize()
                //.height(IntrinsicSize.Min)
        ) {

            Box(
                modifier = modifier
                    .weight(1f) // Prend tout l'espace disponible en laissant afficher les colonnes dessous
                    .fillMaxSize()
                    .border(
                        // Juste pour la preview (A commenter)
                        width = 10.dp,
                        color = Color.Red,
                    )

            ) {

                GlideImage(
                    model = article.sURLPicture,
                    contentDescription = article.sDescriptionPicture,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxSize()
                        //.wrapContentSize()
                        .graphicsLayer {
                            shape = RoundedCornerShape(16.dp) // Coins arrondis // TODO JG : Les coins en bas sont pas arrondi
                        }
                        .border(
                            // Juste pour la preview (A commenter)
                            width = 10.dp,
                            color = Color.Blue,
                        ),
                )

                // TODO Prio - Pas sur que çà soit très académique
                val nHeightAndPadding : Int
                if (bModeDetail){
                    nHeightAndPadding = 30
                }
                else{
                    nHeightAndPadding = 15
                }

                // Superposition du picto cœur avec un nombre entier
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd) // Aligner en bas à droite de l'image
                        .padding(nHeightAndPadding.dp)
                ) {



                    Row(
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(30.dp)
                            )
                            .wrapContentSize()
                            .padding(10.dp)
                            .height(nHeightAndPadding.dp)

                    ){

                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            modifier = Modifier
                                .wrapContentHeight(),
                            contentDescription = stringResource(R.string.icone_coeur),
                            tint = colorHeart // TODO : J'ai 2 fichiers de couleurs Color.kt et colors.xml : lequel utiliser ?
                        )

                        Spacer(modifier = Modifier.size(4.dp))

                        Text(
                            text = article.nNbLikes.toString(),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = typo
                        )

                    }

                    /*
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = stringResource(R.string.icone_coeur),
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp) // Taille de l'icône du cœur
                    )

                    Spacer(modifier = Modifier.width(4.dp)) // Espacement entre l'icône et le texte

                    Text(
                        text = "123", // Nombre entier à afficher
                        color = Color.White,
                        fontSize = 14.dp
                    )
                    */

                }

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

                    Spacer(modifier = Modifier.size(2.dp))

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

    // #Remarque : Preview charge mal les images avec URL du component Glide

    //val myURI = Uri.parse("android.resource://com.openclassrooms.joilfull/" + com.openclassrooms.joilfull.R.drawable.sacpreview)

    val article = Article(
            nIDArticle = 0,
            sURLPicture = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/1.jpg",
            //sURLPicture = myURI.toString(),
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

// Preview dans les items
@Preview(name = "Item Mode",showBackground = true, heightDp = 250, widthDp = 198)
@Composable
fun ArticleItemComposablePreviewItemMode() {

    val article = Article(
        nIDArticle = 0,
        sURLPicture = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/1.jpg",
        //sURLPicture = myURI.toString(),
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
            bModeDetail = false,
            onArticleClickP = {}
        )
    }
}



