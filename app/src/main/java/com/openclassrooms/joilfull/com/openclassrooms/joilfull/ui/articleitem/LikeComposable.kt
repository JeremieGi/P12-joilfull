package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    nNbLikeInitP : Int,
    bInitLike : Boolean,
    onClickLikeP : (bValLike : Boolean) -> Unit,
    bIsClickableP : Boolean
){

    val currentContext = LocalContext.current

    // Durée de vie de ces valeurs = recomposition du composable
    var bLikeValue by remember { mutableStateOf(bInitLike) }
    var nNbLikeValue by remember { mutableStateOf(nNbLikeInitP) }


    var sAccessibilityMessage = stringResource(R.string.cet_article_a_c_urs, nNbLikeValue.toString())
    if (bLikeValue){
        sAccessibilityMessage += stringResource(R.string.cliquer_ici_pour_retirer_votre_like)
    }
    else{
        sAccessibilityMessage += stringResource(R.string.cliquer_ici_pour_liker_cet_article)
    }

    Surface(
        modifier = modifier
            // Then permet le remplace de Modifier
            .then(
                // Si l'émément est clickable
                if (bIsClickableP) {

                    modifier
                        .clickable(
                            onClick = {
                                bLikeValue = !bLikeValue

                                // Appel au ViewModel mais on ne rafraichit pas tout l'écran
                                // juste le Composable à l'aide des variables by remember
                                onClickLikeP(bLikeValue)

                                val sMessageToast: String
                                if (bLikeValue) {
                                    sMessageToast = currentContext.getString(R.string.article_ajout_aux_favoris)
                                    nNbLikeValue++
                                } else {
                                    sMessageToast = currentContext.getString(R.string.article_retir_des_favoris)
                                    nNbLikeValue--
                                }
                                Toast
                                    .makeText(currentContext, sMessageToast, Toast.LENGTH_LONG)
                                    .show()
                            }
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
                imageVector = if (bLikeValue){
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
                text = nNbLikeValue.toString(),
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
        nNbLikeInitP = 10,
        bInitLike = true,
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
        nNbLikeInitP = 1000,
        bInitLike = false,
        onClickLikeP = {},
        bIsClickableP = false
    )

}

