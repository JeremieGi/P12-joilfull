package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.CTE_MIN_SIZE
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester


// Ce point d'entrée est utilisé uniquement pour les petits écrans
@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    articleId: Int,
    viewModelArticle: ArticleViewModel = hiltViewModel(), // View Model depuis le graph de navigation

) {

    //val windowSize = getWindowsSize()

    // TODO Denis : Voir ce cas de test : Est ce vraiment comme çà qu'il faut faire pour éviter de recharger l'article
    // Si l'article n'est pas chargé
    if (viewModelArticle.currentArticle==null){

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
                //bModeTablet = bTablet(windowSize),
                nIDRessourceAvatarP = viewModelArticle.getCurrentUserAvatar(),
                onClickBackP = { navController.popBackStack() },
                onClickSendNoteP = viewModelArticle::sendNoteAndComment,
                onClickLikeP = viewModelArticle::setLike
            )

        }


    }

    else{
        // Cas d'une rotation par exemple
        // Pas besoin de recharger l'article qu'on a déjà dans le viewModel

        ArticleItemDetailComposable(
            modifier = modifier,
            articleP = viewModelArticle.currentArticle!!, // TODO Question Denis : test nullité plus haut mais il faut quand même !!
            onClickLikeP = viewModelArticle::setLike,
            onClickBackP = { navController.popBackStack() },
            onClickSendNoteP = viewModelArticle::sendNoteAndComment,
            nIDRessourceAvatarP = viewModelArticle.getCurrentUserAvatar(),
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
    nIDRessourceAvatarP : Int,
    onClickBackP : (() -> Unit)?,
    onClickSendNoteP : (fNote:Float , sComment : String) -> Unit,
    onClickLikeP : (bValLike : Boolean) -> Unit
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

            ArticleItemDetailComposable(
                modifier = modifier,
                articleP = article,
                nIDRessourceAvatarP = nIDRessourceAvatarP,
                onClickLikeP = onClickLikeP,
                onClickBackP = onClickBackP,
                onClickSendNoteP = onClickSendNoteP
            )
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ArticleItemDetailComposable(
    modifier: Modifier = Modifier,
    articleP : Article,
    nIDRessourceAvatarP : Int,
    onClickLikeP : (bValLike : Boolean) -> Unit,
    onClickBackP : (() -> Unit)?,
    onClickSendNoteP : (fNote:Float , sComment : String) -> Unit
){

 //   val focusRequester = remember { FocusRequester() }
 //   val lifecycleOwner = LocalLifecycleOwner.current

    // TODO Denis : Je n'arrive pas à redéfinir correctement l'ordre de focus ici

    /*
    // Observer de cycle de vie pour demander le focus au onResume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Demande le focus lorsque l'écran est visible
                focusRequester.requestFocus()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {F
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
*/

    Column(
        modifier = modifier
            .padding(
                horizontal = 10.dp
            )
    ){

        Box(
            modifier = Modifier
                .weight(8f)
                //.focusable()

        ) {


            ArticleItemSimpleComposable(
                modifier = Modifier
                    ,
                article = articleP,
                bModeDetail = true,
                onArticleClickP = {}, // OnClick neutralisé
                onClickLikeP = onClickLikeP
            )

            // Bouton visible uniquement lors de l'appel à ArticleScreen (c'est à dire utilisation de navController)
            //if (!bModeTablet){
            // Si la lambda onClicBackP est définie
            if (onClickBackP!=null){

                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp) // Décalage de 10dp du coin supérieur gauche
                        .background(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .sizeIn(
                            minWidth = CTE_MIN_SIZE,
                            minHeight = CTE_MIN_SIZE
                        )
                        //.focusRequester(focusRequester)
                        .focusable()
                    ,
                    //.background(Color.Red)

                    onClick = onClickBackP

                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.TwoTone.ArrowBack,
                        contentDescription = stringResource(R.string.retour),
                        tint = MaterialTheme.colorScheme.onSurface // Utilise la couleur du thème
                    )
                }

            }


        }


        Text(
            modifier = Modifier
                .padding(top = 5.dp)
                .wrapContentSize()
                .sizeIn(
                    minWidth = CTE_MIN_SIZE,
                    minHeight = CTE_MIN_SIZE
                ),
            text = articleP.sDescriptionArticle
        )

        NotationInputComposable(
            modifier = Modifier
                .padding(
                    vertical = 10.dp
                ),
            nIDUser = 0,    // TODO JG :ID User
            nIDArticle = articleP.nIDArticle,
            nIDRessourceAvatarP = nIDRessourceAvatarP,
            onClickSendNoteP = onClickSendNoteP)

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
            nIDRessourceAvatarP = R.drawable.currentuseravatar,
            onClickBackP = { navController.popBackStack() },
            onClickSendNoteP = {_,_ -> }, // 2 paramètres et retour Unit
            onClickLikeP = {}
        )


    }

}
