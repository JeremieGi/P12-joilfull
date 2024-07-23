package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.openclassrooms.joilfull.Links
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.CTE_MIN_SIZE
import com.openclassrooms.joilfull.model.Article

/**
 * Bouton "Partager"
 */
@Composable
fun ShareButtonComposable(
    modifier: Modifier = Modifier,
    articleP : Article,

    ){

    val currentContext = LocalContext.current

    val sDeepLink = Links.createDeepLink(articleP.nIDArticle)
    val sDefaultMessage = currentContext.getString(R.string.je_partage_cet_article)

    // Gestion de la pop-up de confirmation
    var bShowDialog by rememberSaveable { mutableStateOf(false) }
    var sInputText by rememberSaveable { mutableStateOf(sDefaultMessage) }

    // Icône du bouton de partage
    Box(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp)
            ) // Fond avec coins arrondis
    ) {
        IconButton(
            onClick = {
                // Lance une recomposition pour afficher la pop-up
                bShowDialog = true
            },

            ) {
            Icon(
                modifier = Modifier
                    .padding(10.dp)
                    .sizeIn(
                        minWidth = CTE_MIN_SIZE,
                        minHeight = CTE_MIN_SIZE
                    ),
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.partager_l_article_sur_les_r_seaux),
                tint = MaterialTheme.colorScheme.onSurface // Couleur de l'icône
            )
        }
    }

    // Boîte de dialogue d'alerte
    if (bShowDialog) {

        AlertDialog(
            onDismissRequest = {
                // clic à l'extérieur => ferme la boîte de dialogue sans partager
                bShowDialog = false
            },
            title = {
                Text(text = stringResource(R.string.personnalise_ton_message_de_partage))
            },
            text = {

                Column {

                    // Champs de saisie dans l'AlertDialog
                    TextField(
                        value = sInputText,
                        onValueChange = {
                            sInputText = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Lien non-modifiable
                    Text(text = sDeepLink)

                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Ferme la boîte de dialogue
                        bShowDialog = false

                        // partage le lien avec le texte saisi + deep link
                        val sShareMessage = "$sInputText\n$sDeepLink"
                        shareArticle(sShareMessage, currentContext)

                    }
                ) {
                    Text(stringResource(R.string.partager))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Ferme la boîte de dialogue sans partager
                        bShowDialog = false
                    }
                ) {
                    Text(text = stringResource(R.string.annuler))
                }
            }
        )

    }

}



/**
 * Partage l'article sur les réseaux sociaux
 */
fun shareArticle(sCustomText : String, currentContext : Context) {

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, sCustomText)
        type = "text/plain"
    }

    val currentActivity = (currentContext as Activity)

    currentActivity.startActivity(
        Intent.createChooser(
            shareIntent,
            currentContext.getString(R.string.partager_via)
        )
    )

}