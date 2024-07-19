package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.bTablet
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme


// Ce point d'entrée est utilisé uniquement pour les petits écrans
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    articleId: Int,
    viewModelArticle: ArticleViewModel = hiltViewModel(), // View Model depuis le graph de navigation

) {

    val context = LocalContext.current
    val activity = context as? Activity

    var windowSizeClass : WindowSizeClass? = null
    activity?.let {
        windowSizeClass = calculateWindowSizeClass(activity)
    }

    // Trigger loading article details when articleId changes
    // Premier lancement
    LaunchedEffect(articleId) {
        viewModelArticle.loadArticleByID(articleId)
    }

    val uiState by viewModelArticle.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        ArticleItemContent(
            modifier = Modifier,
            uiState = uiState,
            navController = navController,
            bModeTablet = bTablet(windowSizeClass),
            nIDRessourceAvatar = viewModelArticle.getCurrentUserAvatar(),
            onClickSendNote = viewModelArticle::sendNoteAndComment
        )

    }






}

/**
 * Composant affichant le détails d'un article
 */
@Composable
fun ArticleItemContent(
    modifier: Modifier = Modifier,
    uiState: ArticleUIState,
    navController: NavController,
    bModeTablet : Boolean,
    nIDRessourceAvatar : Int,
    onClickSendNote : (fNote:Float , sComment : String) -> Unit
){

    // En fonction de l'état du viewModel
    when (uiState) {

        is ArticleUIState.NoSelectedArticle -> {
            // Normalement la fenêtre ne doit pas être affichée du tout dans cet état
        }

        // Chargement
        is ArticleUIState.IsLoading -> {
            LoadingComposable(modifier)
        }

        // Récupération des données avec succès
        is ArticleUIState.Success -> {

            //val article = (uiState as ArticleUIState.Success).article
            val article = uiState.article // Kotlin sait que ArticleUIState est un success à cet endroit

            Column(
                modifier = modifier
                    .padding(
                        horizontal = 10.dp
                    )
            ){
                
                Box(
                    modifier = Modifier
                        .weight(8f)
                ) {

                    ArticleItemComposable(
                        modifier = modifier,
                        article = article,
                        bModeDetail = true,
                        onArticleClickP = {} // On Click neutralisé
                    )

                    // Bouton visible uniquement lors de l'appel à ArticleScreen (c'est à dire utilisation de navController)
                    if (!bModeTablet){

                        IconButton(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(10.dp) // padding dans la Box
                            //.background(Color.Red)
                            ,
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.retour),
                                tint = MaterialTheme.colorScheme.onSurface // Utilise la couleur du thème
                            )
                        }
                        
                    }
                }


                Text(
                    modifier = Modifier
                        .wrapContentSize(),
                    text = article.sDescriptionArticle
                )

                NotationInputComposable(
                    modifier = Modifier
                        .padding(
                            vertical = 10.dp
                        ),
                    nIDUser = 0,    // TODO JG :ID User
                    nIDArticle = article.nIDArticle,
                    nIDRessourceAvatar = nIDRessourceAvatar,
                    onClickSendNote = onClickSendNote)

            }

        }

        // Exception
        is ArticleUIState.Error -> {

            val error = uiState.exception.message ?: "Unknown error"
            ErrorComposable(
                modifier=modifier,
                sMessage=error,
                onClickRetryP = { }
            )


        }

    }
}



// Preview ne marche pas avec Hilt et les ViewModel
@Preview(name = "Item Mode",showBackground = true, showSystemUi = true)
@Composable
fun ArticleScreenPreview(){



    JoilfullTheme {

        // Pour tester différents code comme le bouton Back


        /*
         * Si vous souhaitez prévisualiser un composable qui utilise un ViewModel,
         * vous devez créer un autre composable avec les paramètres de ViewModel transmis en tant qu'arguments du composable. De cette façon, vous n'avez pas besoin de prévisualiser le composable qui utilise ViewModel.
         */
        /*

        ArticleScreen(
            navController = navController,
            articleId = 2
        )
        */

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

        val uiStateSuccess = ArticleUIState.Success(article)

        val navController = rememberNavController() // factice

        ArticleItemContent(
            uiState = uiStateSuccess,
            navController = navController,
            bModeTablet = false,
            nIDRessourceAvatar = R.drawable.currentuseravatar,
            onClickSendNote = {_,_ -> Unit}
        )


    }

}
