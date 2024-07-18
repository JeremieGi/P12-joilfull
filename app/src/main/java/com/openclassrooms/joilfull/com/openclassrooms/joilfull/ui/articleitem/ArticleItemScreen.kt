package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.ErrorComposable
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.LoadingComposable
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme


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

    //val onLoadArticle = { viewModel.loadArticleByID(articleId) }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        ArticleItemContent(
            //modifier = Modifier,
            uiState = uiState,
            //onClickErrorRetryP = onLoadArticle
        )

        // Bouton visible uniquement lors de l'appel à ArticleScreen (c'est à dire utilisation de navController)
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
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

@Composable
fun ArticleItemContent(
    uiState: ArticleUIState,
    modifier: Modifier = Modifier
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
            val article = uiState.article

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

    /*
     * Si vous souhaitez prévisualiser un composable qui utilise un ViewModel,
     * vous devez créer un autre composable avec les paramètres de ViewModel transmis en tant qu'arguments du composable. De cette façon, vous n'avez pas besoin de prévisualiser le composable qui utilise ViewModel.
     */

    JoilfullTheme {

        // Pour tester différents code comme le bouton Back

        /*
        val navController = rememberNavController() // factice
        ArticleScreen(
            navController = navController,
            articleId = 2
        )
        */

        IconButton(
            onClick = {},
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.retour),
                tint = MaterialTheme.colorScheme.onSurface // Utilise la couleur du thème
            )
        }

    }

}
