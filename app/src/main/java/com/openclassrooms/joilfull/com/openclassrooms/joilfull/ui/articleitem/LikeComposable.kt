package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.CTE_MIN_SIZE
import com.openclassrooms.joilfull.ui.theme.colorHeart

/**
 * Compsable qui affiche les likes
 */
@Composable
fun LikeComposable(
    modifier : Modifier,
    bModeItemOnRight : Boolean,
    nNbLikeP : Int,
    bInitLikeP : Boolean,
    onClickLikeP : (bValLike : Boolean) -> Unit,
    bIsClickableP : Boolean
){

    //val currentContext = LocalContext.current


    // Durée de vie de ces valeurs = recomposition du composable
    var bLikeRemember by remember { mutableStateOf(bInitLikeP) }
    var nNbLikeRemember by remember { mutableIntStateOf(nNbLikeP) }


    var sAccessibilityMessage = stringResource(R.string.cet_article_a_c_urs, nNbLikeRemember.toString())
    if (bLikeRemember){
        sAccessibilityMessage += stringResource(R.string.cliquer_ici_pour_retirer_votre_like)
    }
    else{
        sAccessibilityMessage += stringResource(R.string.cliquer_ici_pour_liker_cet_article)
    }

    // Variables affichées à l'écran
    val bLikeDisplay : Boolean
    val nNbLikeDisplay : Int

    // TODO Denis à montrer : 2 façons de gérer le click sur le like (code complexe je trouve)
    val onClickLikeFinal : () -> Unit
    if (bModeItemOnRight){
        // En tablette, on appelle le viewModel qui va reclencher un Ui State et redessiner toute la fenêtre (car la liste doit se mettre à jour aussi)
        onClickLikeFinal = {

            val bNewValLike = !bInitLikeP

            // Appel au ViewModel on rafraichit tout l'écran
            onClickLikeP(bNewValLike)

        }
        bLikeDisplay = bInitLikeP
        nNbLikeDisplay = nNbLikeP
    }
    else{
        // Sur téléphone, on appelle le viewModel qui va enregitrer le like en mémoire mais ne va pas déclencher de revomposition via le UI State
        // On utilisera les variables remember pour mettre à jour l'interface

        onClickLikeFinal = {

            // Déclenchera une recomposition
            bLikeRemember = !bLikeRemember

            // Appel au ViewModel sans rafraichissement
            onClickLikeP(bLikeRemember)

            if (bLikeRemember){
                nNbLikeRemember++
            }
            else{
                nNbLikeRemember--
            }
        }

        bLikeDisplay = bLikeRemember
        nNbLikeDisplay = nNbLikeRemember

    }

    Surface(
        modifier = modifier
            // Then permet le remplace de Modifier
            .then(
                // Si l'émément est clickable
                if (bIsClickableP) {

                    modifier
                        .clickable(
                            onClick = onClickLikeFinal
                        )
                        .semantics(mergeDescendants = true) {}
                        .clearAndSetSemantics {
                            contentDescription = sAccessibilityMessage
                        }
                } else {
                    modifier
                }
            )
            .sizeIn(
                minWidth = CTE_MIN_SIZE,
                minHeight = CTE_MIN_SIZE
            )

        //.wrapContentSize()
            //.height(nHeight.dp)
            //.heightIn(min = 40.dp, max = 200.dp) // marche pas quand height est forcé

            /*
            .border(width = 2.dp, color = Color.Black)
            */
        ,
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp // Élévation de la Surface
    ){


        Row(
            modifier = Modifier
                .background(
                    color = Color.White,
                )
                .wrapContentSize()
                .padding(8.dp),  // Espace entre le bord de la surface est le Row
                //.height(nHeightP.dp),
            verticalAlignment = Alignment.CenterVertically,

            ){


            Icon(
                imageVector = if (bLikeDisplay){
                        Icons.Filled.Favorite
                    } else{
                        Icons.Filled.FavoriteBorder
                    },
                modifier = Modifier
                    .wrapContentSize(),
                contentDescription = stringResource(R.string.icone_coeur),
                tint = colorHeart // TODO Denis 2 : J'ai 2 fichiers de couleurs Color.kt et colors.xml : lequel utiliser ?
            )

            Spacer(modifier = Modifier.size(4.dp))


            Text(
                modifier = Modifier
                    .wrapContentSize()
                ,
                text = nNbLikeDisplay.toString(),
                color = Color.Black, //MaterialTheme.colorScheme.onSurface, // Sinon problème de contraste en theme Dark
                textAlign = TextAlign.Center
            )


        }

    }




}


@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES/*, showBackground = true*/)
@Composable
fun LikeComposablePreviewItem() {

    LikeComposable(
        modifier = Modifier
            .height(40.dp),
        bModeItemOnRight = false,
        nNbLikeP = 10,
        bInitLikeP = true,
        onClickLikeP = {},
        bIsClickableP = true
    )

}


@Preview(name = "Light Mode")
@Composable
fun LikeComposablePreviewTablet() {

    LikeComposable(
        modifier = Modifier
            .height(50.dp),
        bModeItemOnRight = false,
        nNbLikeP = 1000,
        bInitLikeP = false,
        onClickLikeP = {},
        bIsClickableP = false
    )

}

