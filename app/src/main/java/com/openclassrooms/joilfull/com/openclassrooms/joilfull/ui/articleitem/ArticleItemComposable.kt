package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.sDisplayPrice
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ArticleItemSimpleComposable(
    modifier: Modifier = Modifier,
    article : Article,
    bModeDetail : Boolean,  // Vrai => Mode détails, faux => Mode Item
    onArticleClickP : (Article) -> Unit,
    onClickLikeP : (bValLike : Boolean) -> Unit
) {

    val currentContext = LocalContext.current

    // Adaptation des polices si on est dans la fenêtre de détail ou de liste
    val typo : TextStyle = if (bModeDetail){
        MaterialTheme.typography.titleLarge
    }
    else{
        MaterialTheme.typography.titleSmall
    }


    Card(
        modifier = modifier
            .then(
                if (!bModeDetail) { // Clic sur l'article qu'en mode liste
                    modifier.clickable()
                    {
                        onArticleClickP(article)
                    }
                } else {
                    modifier
                }
            ),

        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent // sinon pardéfaut MaterialTheme.colorScheme.surfaceContainer
        )

    ) {
        Column ()
        {

            // Box pour superposition
            Box(
                modifier = Modifier
                    .weight(1f) // Prend tout l'espace disponible en laissant afficher les colonnes dessous
                    //.fillMaxSize()
            ) {

                // Surface spécialement dédié pour arrondir les coins de la GlideImage
                Surface(
                    shape = RoundedCornerShape(16.dp),  // Coins arrondis

                ){

                    GlideImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Gray)       // Pour les photos plus petites (comble l'espace)
                            //.focusTarget()
                        ,
                        model = article.sURLPicture,
                        contentDescription = article.sDescriptionPicture,
                        contentScale = ContentScale.FillWidth, // FillBounds = Etiré / Fit = Toute la photo rentre sans déformation
                    )

                }


                // Superposition du picto cœur avec un nombre entier
                LikeComposable(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)         // Aligner en bas à droite de l'image,
                        .padding(10.dp)                     // Ecart avec le bas droit
                        .sizeIn(
                            minHeight = 60.dp,               // Hauteur minimale
                            // maxHeight = 300.dp
                        )
                        .fillMaxHeight(0.1f)          // 1/10 de hauteur du contenant
                   ,
                    nNbLikeInitP = article.getNbLikes(),
                    bInitLike = article.bFavorite,
                    onClickLikeP = onClickLikeP,
                    bIsClickableP = bModeDetail // Clickable qu'en mode détail
                )

                if (bModeDetail){


                    // Box pour l'IconButton sinon impossible de mettre un padding
                    // (vu que le padding de la Box fait référence à la Box parente - Ecart avec le coin haut / Droit)
                    ShareButtonComposable(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp) //  Ecart avec le coin haut / Droit
                        ,
                        articleP = article
                    )



                }


            }

            Row(
                modifier = Modifier.padding(
                    top = 5.dp
                ),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    modifier = Modifier.weight(1f),
                    text = article.sName,
                    style =  typo,
                    maxLines = 1,                       // une ligne
                    overflow = TextOverflow.Ellipsis    // avec ellipse ...
                )

                NoteComposable(
                    modifier = Modifier.wrapContentWidth(),
                    sNote = article.sAverageNote(currentContext),
                    textStyle = typo
                )

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
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Un peu plus grisé que le prix
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
        //sName = "Sac à main orange",
        sName = "Sac à main orange très long pour tester largeur mini",
        sCategory = "ACCESSORIES", // Enumération ici : pas trop d'intéret si jamais le WS renvoie une nouvelle catégorie
        nNbLikesInit = 56,
        dPrice = 69.99,
        dOriginalPrice = 99.00
    )


    JoilfullTheme {
        ArticleItemSimpleComposable(
            article = article,
            bModeDetail = true,
            onArticleClickP = {},
            onClickLikeP = {}
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
        nNbLikesInit = 56,
        dPrice = 69.99,
        dOriginalPrice = 99.00
    )

    article.setFavorite(bFavoriteP = true)


    JoilfullTheme {
        ArticleItemSimpleComposable(
            article = article,
            bModeDetail = false,
            onArticleClickP = {},
            onClickLikeP = {}
        )
    }
}


