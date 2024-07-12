package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme

/*
 * Error in DialogAlert
 */
@Composable
fun ErrorDialog(
    sMessage : String,
    modifier: Modifier = Modifier
) {

    var bShowDialog by rememberSaveable { mutableStateOf( sMessage.isNotEmpty() ) }

    if (bShowDialog){

        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back button.
                bShowDialog = false
            },
            title = { Text(text = stringResource(R.string.erreur)) },
            text = { Text(text = sMessage) },
            modifier = modifier,

            confirmButton = {
                TextButton(onClick = { bShowDialog = false }) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        )

    }


}

@Preview(showBackground = true)
@Composable
fun ErrorDialogPreview() {
    JoilfullTheme {
        ErrorDialog(stringResource(R.string.erreur_pr_visualis_e))
    }
}
