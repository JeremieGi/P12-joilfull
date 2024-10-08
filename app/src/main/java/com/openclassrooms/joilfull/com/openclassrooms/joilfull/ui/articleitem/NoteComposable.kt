package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.ui.theme.colorStar


@Composable
fun NoteComposable(
    modifier: Modifier = Modifier,
    sNote : String,
    textStyle : TextStyle,

) {

    val currentContext = LocalContext.current

    // Affichage de la note moyenne
    Row (
        modifier = modifier
            .wrapContentWidth()    // Répercution de la taille choisie
            .semantics(mergeDescendants = true) {}
            .clearAndSetSemantics {
                contentDescription = currentContext.getString(R.string.note_de_sur_5, sNote)
            },
        verticalAlignment = Alignment.CenterVertically,

    ){

        Icon(
            imageVector = Icons.Filled.Star,
            modifier = Modifier
                .wrapContentSize() // Utilise la largeur de la moitié du composant Row
            ,
            contentDescription = null, //stringResource(R.string.etoile),
            tint = colorStar
        )

        Spacer(modifier = Modifier.size(3.dp))

        Text(
            text = sNote,
            style = textStyle,
            modifier = Modifier
                .wrapContentSize()
            ,
        )

    }

}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES/*, showBackground = true*/)
@Composable
fun NoteComposablePreviewItem() {

    // Adaptation des polices si on est dans la fenêtre de détail ou de liste
    val typo = MaterialTheme.typography.titleSmall

    NoteComposable(
        modifier = Modifier
            .height(30.dp)
            .width(100.dp),
        sNote = "6.8",
        textStyle = typo
    )

}


@Preview(name = "Light Mode")
@Composable
fun NoteComposablePreviewTablet() {

    val typo = MaterialTheme.typography.titleLarge

    NoteComposable(
        modifier = Modifier
            .size(
                width = 100.dp,
                height = 100.dp),
        sNote = "5",
        textStyle = typo
    )

}
