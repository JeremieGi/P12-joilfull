package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.sDisplayPrice
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import com.openclassrooms.joilfull.ui.theme.colorStar


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
            ) {

                // Surface spécialement dédié pour arrondir les coins de la GlideImage
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),

                    shape = RoundedCornerShape(16.dp),  // Coins arrondis

                ){

                    GlideImage(
                        model = article.sURLPicture,
                        contentDescription = article.sDescriptionPicture,
                        contentScale = ContentScale.FillWidth, // FillBounds = Etiré / Fit = Toute la photo rentre sans déformation
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray)       // Pour les photos plus petites (comble l'espace)

                        /*
                        .border(
                            // Juste pour la preview (A commenter)
                            width = 10.dp,
                            color = Color.Blue,
                        )
                        */
                    )

                }




                // TODO Denis - Pas sur que çà soit très académique
                // J'aimerais faire quelque chose de proportionnel au contenant
                val nHeight : Int
                val nPadding : Int
                if (bModeDetail){
                    nHeight = 50
                    nPadding = 30
                }
                else{
                    nHeight = 40
                    nPadding = 15
                }

                // Superposition du picto cœur avec un nombre entier
                LikeComposable(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)     // Aligner en bas à droite de l'image,
                        .padding(nPadding.dp) // Ecart avec le bas droit
                        .height(nHeight.dp)
                    ,
                    sNbLikeP = article.nNbLikes.toString()
                )

                if (bModeDetail){

                    IconButton(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                        //.background(Color.Red)
                        ,
                        onClick = {
                            // TODO JG : Partage sur les réseaux
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(R.string.partager),
                            tint = MaterialTheme.colorScheme.onSurface // Utilise la couleur du thème
                        )
                    }

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
                    style =  typo,
                    //maxLines = 1,                       // une ligne
                    //overflow = TextOverflow.Ellipsis    // avec ellipse ...
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
                        tint = colorStar // TODO Denis : J'ai 2 fichiers de couleurs Color.kt et colors.xml : lequel utiliser ?
                    )

                    Spacer(modifier = Modifier.size(2.dp))

                    Text(
                        text = "X.X", // TODO JG : Moyenne des notes
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


