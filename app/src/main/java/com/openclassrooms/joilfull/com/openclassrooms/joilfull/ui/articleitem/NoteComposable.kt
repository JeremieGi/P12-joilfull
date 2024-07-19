package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.colorStar


@Composable
fun NoteComposable(
    modifier: Modifier = Modifier,
    sNote : String,
    textStyle : TextStyle,

) {

    // Affichage de la note moyenne
    Row (
        modifier = modifier
            .wrapContentWidth(),    // Répercution de la taille choisie
        verticalAlignment = Alignment.CenterVertically
    ){

        Icon(
            imageVector = Icons.Filled.Star,
            modifier = Modifier
                .wrapContentSize() // Utilise la largeur de la moitié du composant Row
            ,
            contentDescription = stringResource(R.string.etoile),
            tint = colorStar // TODO Denis 2 : J'ai 2 fichiers de couleurs Color.kt et colors.xml : lequel utiliser ?
        )

        Spacer(modifier = Modifier.size(3.dp))

        Text(
            text = sNote,
            style = textStyle,
            modifier = Modifier
                .wrapContentSize()


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
