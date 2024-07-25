package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

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
import androidx.compose.material.icons.automirrored.twotone.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.CTE_MIN_SIZE
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme


// Ce point d'entrée est utilisé uniquement pour les petits écrans
@Composable
fun ArticleScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    articleId: Int,
    viewModelArticle: ArticleViewModel = hiltViewModel(), // View Model depuis le graph de navigation

) {

    //val windowSize = getWindowsSize()


    // TODO JG : Voir si on peut pas virer çà
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
                nIDCurrentUserP = viewModelArticle.getCurrentUserID(),
                nIDRessourceAvatarP = viewModelArticle.getCurrentUserAvatar(),
                onClickBackP = { navController.popBackStack() },
                onClickSendNoteP = viewModelArticle::sendNoteAndComment,
                onClickLikeP = viewModelArticle::setLike,
                unselectArticle = {} // Depuis ArticleScreen, on a ouvert la fenêtre via le NavControler
            )

        }


    }

    else{
        // Cas d'une rotation par exemple
        // Pas besoin de recharger l'article qu'on a déjà dans le viewModel

        ArticleItemDetailComposable(
            modifier = modifier,
            articleP = viewModelArticle.currentArticle!!, // TODO Question Denis : test nullité plus haut mais il faut quand même !!
            nIDCurrentUserP = viewModelArticle.getCurrentUserID(),
            onClickLikeP = viewModelArticle::setLike,
            onClickBackP = { navController.popBackStack() },
            onClickSendNoteP = viewModelArticle::sendNoteAndComment,
            nIDRessourceAvatarP = viewModelArticle.getCurrentUserAvatar(),
            unselectArticle = {} // Depuis ArticleScreen, on a ouvert la fenêtre via le NavControler
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
    nIDCurrentUserP : Int,
    nIDRessourceAvatarP : Int,
    onClickBackP : (() -> Unit)?,
    onClickSendNoteP : (nNote:Int , sComment:String) -> Unit,
    onClickLikeP : (bValLike : Boolean) -> Unit,
    unselectArticle : (() -> Unit)
){



    // En fonction de l'état du viewModel
    when (uiState) {

        // Chargement
        is ArticleUIState.IsLoadingArticle -> {
            LoadingComposable(modifier)
        }

        // Récupération des données avec succès
        is ArticleUIState.SuccessArticle -> {

            //val article = (uiState as ArticleUIState.Success).article
            val article = uiState.article // Kotlin sait que ArticleUIState est un success à cet endroit

            ArticleItemDetailComposable(
                modifier = modifier,
                articleP = article,
                nIDCurrentUserP = nIDCurrentUserP,
                nIDRessourceAvatarP = nIDRessourceAvatarP,
                onClickLikeP = onClickLikeP,
                onClickBackP = onClickBackP,
                onClickSendNoteP = onClickSendNoteP,
                unselectArticle = unselectArticle
            )
        }

        // Exception
        is ArticleUIState.ErrorArticle -> {

            val error = uiState.exception.message ?: "Unknown error"
            ErrorComposable(
                modifier=modifier,
                sMessage=error,
                onClickRetryP = { }
            )


        }

    }
}

/**
 * // TODO Question Denis : Il est préconisé de ne pas passé le viewModel en paramètre pour des questions de perf
 * // mais du coup je me retrouve à hisser toutes les méthodes dont j'ai besoin... et il y en a beaucoup
 */

@Composable
fun ArticleItemDetailComposable(
    modifier: Modifier = Modifier,
    articleP : Article,
    nIDCurrentUserP : Int,
    nIDRessourceAvatarP : Int,
    onClickLikeP : (bValLike : Boolean) -> Unit,
    onClickBackP : (() -> Unit)?,
    onClickSendNoteP : (nNote:Int , sComment:String) -> Unit,
    unselectArticle : (() -> Unit)
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

    // On est en mode tablette si pas de backstack
    val bTablet = (onClickBackP==null)

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
                article = articleP,
                bModeDetail = true,
                bTablet = bTablet,
                onArticleClickP = {}, // OnClick neutralisé
                onClickLikeP = onClickLikeP
            )

            // Bouton visible uniquement lors de l'appel à ArticleScreen (c'est à dire utilisation de navController)
            //if (!bModeTablet){
            // Si la lambda onClicBackP est définie

            val icon : ImageVector
            val onBackOrClose : (() -> Unit)
            if (onClickBackP!=null){
                icon = Icons.AutoMirrored.TwoTone.ArrowBack
                onBackOrClose = onClickBackP
            }else{
                icon = Icons.Default.Close
                onBackOrClose = unselectArticle
            }

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

                onClick = onBackOrClose

            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.retour),
                    tint = MaterialTheme.colorScheme.onSurface // Utilise la couleur du thème
                )
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

        val existingFeedback =  articleP.getExistingNote(nIDCurrentUserP)

        NotationInputComposable(
            modifier = Modifier
                .padding(
                    vertical = 10.dp
                ),
            nExistingNoteP = existingFeedback?.nNote ?:0,
            sExistingCommentP = existingFeedback?.sComment ?:"",
            nIDRessourceAvatarP = nIDRessourceAvatarP,
            onClickSendNoteP = onClickSendNoteP,
            onClickBackP = onClickBackP,
            unselectArticle = unselectArticle)

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
            nNbLikesInit = 56,
            dPrice = 69.99,
            dOriginalPrice = 99.00
        )



        val uiStateSuccess = ArticleUIState.SuccessArticle(article)

        val navController = rememberNavController() // factice

        ArticleItemContent(
            uiState = uiStateSuccess,
            nIDCurrentUserP = 1,
            nIDRessourceAvatarP = R.drawable.currentuseravatar,
            onClickBackP = { navController.popBackStack() },
            onClickSendNoteP = {_,_ -> }, // 2 paramètres et retour Unit
            onClickLikeP = {},
            unselectArticle = {} // Depuis ArticleScreen, on a ouvert la fenêtre via le NavControler
        )


    }

}
