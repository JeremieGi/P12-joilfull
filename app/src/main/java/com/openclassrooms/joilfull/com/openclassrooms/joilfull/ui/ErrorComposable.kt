package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui


import android.app.Activity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme


/**
 * Error in DialogAlert
 * @param idArticle : 0 => Contexte d'une liste d'article => on recharge la liste sinon l'ID de l'article
 */
@Composable
fun ErrorComposable(
    sMessage: String,
    modifier: Modifier = Modifier,
    onClickRetryP: () -> Unit
) {

    val activity = (LocalContext.current as Activity)

    // if (bShowDialog){
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back button.
            //bShowDialog = false
            activity.finish()
        },
        title = { Text(text = stringResource(R.string.erreur)) },
        text = { Text(text = sMessage) },
        modifier = modifier,

        dismissButton =  {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.fermer))
            }
        },


        confirmButton = {

            // Si une lambda est passé en paramètre
            if (onClickRetryP != {}){
                TextButton(onClick = onClickRetryP ) {
                    Text(text = stringResource(R.string.reessayer))
                }
            }

        }
    )



}

// TODO : Denis 2 => Preview simple qui échoue
@Preview(showBackground = true)
@Composable
fun ErrorDialogPreview() {

    JoilfullTheme {
        ErrorComposable(
            stringResource(R.string.erreur_pr_visualis_e),
            onClickRetryP = { }
        )
    }
}
