package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.app.Activity
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.CTE_MIN_SIZE
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.bDisplayItemOnRight
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.logCompose
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

    LaunchedEffect(articleId) {
        // Coroute exécutée lorsque articleId change
        // Coroute exécutée aussi à la rotation de l'écran : Lorsqu'une activité ou un fragment est recomposé en réponse à un changement de configuration, Compose recompose toute l'UI visible.
        viewModelArticle.loadArticleByID(articleId)
        logCompose("ArticleScreen : loadArticleByID($articleId)")
    }

    val uiState by viewModelArticle.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        val bModeItemOnRight = bDisplayItemOnRight(/*windowSize*/LocalContext.current)

        val onBackOrCloseP : (() -> Unit)
        if (bModeItemOnRight){
            onBackOrCloseP = viewModelArticle::unselectArticle
        }
        else{
            onBackOrCloseP = { navController.popBackStack() }
        }

        //val nRate by viewModelArticle.nCurrentNote

        ArticleUIStateComposable(
            modifier = Modifier,
            uiState = uiState,
            bModeItemOnRight = bModeItemOnRight,
            nIDCurrentUserP = viewModelArticle.getCurrentUserID(),
            nIDRessourceAvatarP = viewModelArticle.getCurrentUserAvatar(),
            onBackOrCloseP = onBackOrCloseP,
            onClickSendNoteP = viewModelArticle::sendNoteAndComment,
            onClickLikeP = viewModelArticle::setLike
        )

    }



}

/**
 * Composant affichant le détails d'un article
 */
@Composable
fun ArticleUIStateComposable(
    modifier: Modifier = Modifier,
    uiState: ArticleUIState,
    bModeItemOnRight : Boolean,
    nIDCurrentUserP : Int,
    nIDRessourceAvatarP : Int,
    onBackOrCloseP : (() -> Unit),
    onClickSendNoteP : (nNote:Int , sComment:String) -> Unit,
    onClickLikeP : (bValLike : Boolean) -> Unit
){

    // Comportement étrange : Au clic d'un élémént de la liste sur téléphone : 3 appels : Loading puis 2 fois Selected article (au lieu de 1)


    logCompose("ArticleUIStateComposable : Changement UIState : $uiState")

    // En fonction de l'état du viewModel
    when (uiState) {

        is ArticleUIState.NoneArticleSelected -> {
            // Affiche rien
        }

        // Chargement
        is ArticleUIState.IsLoadingArticle -> {
            LoadingComposable(modifier)
        }

        // Récupération des données avec succès
        is ArticleUIState.ArticleSelected -> {

            //val article = (uiState as ArticleUIState.Success).article
            val article = uiState.article // Kotlin sait que ArticleUIState est un success à cet endroit

            ArticleItemDetailComposable(
                modifier = modifier,
                articleP = article,
                bModeItemOnRight = bModeItemOnRight,
                nIDCurrentUserP = nIDCurrentUserP,
                nIDRessourceAvatarP = nIDRessourceAvatarP,
                onClickLikeP = onClickLikeP,
                onBackOrCloseP = onBackOrCloseP,
                onClickSendNoteP = onClickSendNoteP
            )
        }

        // Exception
        is ArticleUIState.ErrorArticle -> {

            val activity = (LocalContext.current as Activity)

            val error = uiState.exception.message ?: "Unknown error"
            ErrorComposable(
                modifier=modifier,
                sMessage=error,
                onClickRetryP = { },
                closeActivity = activity::finish
            )


        }

    }
}

/**
 * // Il est préconisé de ne pas passé le viewModel en paramètre pour des questions de perf
 * // mais du coup je me retrouve à hisser toutes les méthodes dont j'ai besoin... et il y en a beaucoup ..
 */

@Composable
fun ArticleItemDetailComposable(
    modifier: Modifier = Modifier,
    articleP : Article,
    bModeItemOnRight : Boolean,
    nIDCurrentUserP : Int,
    nIDRessourceAvatarP : Int,
    onClickLikeP : (bValLike : Boolean) -> Unit,
    onBackOrCloseP : (() -> Unit),
    onClickSendNoteP : (nNote:Int , sComment:String) -> Unit,
){


    // TODO Denis : Je n'arrive pas à redéfinir correctement l'ordre de focus ici (Embetant pour l'accessibilité)


    Column(
        modifier = modifier
            .padding(
                horizontal = 10.dp
            )
    ){

        Box(
            modifier = Modifier
                /*.heightIn(
                    min = 200.dp
                )*/
                .weight(8f)

        ) {


            val icon : ImageVector
            if (!bModeItemOnRight){
                // Retour en mode téléphone
                icon = Icons.AutoMirrored.TwoTone.ArrowBack
            }else{
                // Masque la partie Article en mode tablette
                icon = Icons.Default.Close
            }

            IconButton(
                modifier = Modifier
                    .zIndex(1f)     // Elément placé devant l'image même s'il est déclaré avant (permet un bon ordre de focus pour l'accessibilité)
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

                ,
                onClick = onBackOrCloseP

            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(R.string.retour),
                    tint = MaterialTheme.colorScheme.onSurface // Utilise la couleur du thème
                )
            }


            ShareButtonComposable(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp) //  Ecart avec le coin haut / Droit
                    .zIndex(1f)     // Elément placé devant l'image même s'il est déclaré avant (permet un bon ordre de focus pour l'accessibilité)
                ,
                articleP = articleP
            )


            ArticleItemSimpleComposable(
                article = articleP,
                bModeDetail = true,
                bModeItemOnRight = bModeItemOnRight,
                onArticleClickP = {}, // OnClick neutralisé
                onClickLikeP = onClickLikeP
            )




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
            nExistingNoteP = existingFeedback?.nNote ?:0, // nRateP => pour test de la solution ViewModel
            sExistingCommentP = existingFeedback?.sComment ?:"",
            nIDRessourceAvatarP = nIDRessourceAvatarP,
            onClickSendNoteP = onClickSendNoteP,
            onBackOrCloseP = onBackOrCloseP,
            //updateNoteOnViewModelP = updateNoteOnViewModelP
            )

    }

}

@Preview(name = "Item Mode",showBackground = true, showSystemUi = true)
@Composable
fun ArticleUIStateComposablePreview(){

    JoilfullTheme {

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



        val uiStateSuccess = ArticleUIState.ArticleSelected(article)

        val navController = rememberNavController() // factice

        ArticleUIStateComposable(
            uiState = uiStateSuccess,
            nIDCurrentUserP = 1,
            nIDRessourceAvatarP = R.drawable.currentuseravatar,
            bModeItemOnRight = true,
            onBackOrCloseP = { navController.popBackStack() },
            onClickSendNoteP = {_,_ -> }, // 2 paramètres et retour Unit
            onClickLikeP = {}
        )


    }

}
