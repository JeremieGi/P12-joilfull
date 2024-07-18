package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.colorStar


@Composable
fun NoteComposable(
    modifier: Modifier = Modifier,
    sNote : String,
) {

    // Affichage de la note moyenne
    Row (
        modifier = modifier
            .wrapContentHeight()
            ,
        verticalAlignment = Alignment.CenterVertically
    ){


        Icon(
            imageVector = Icons.Filled.Star,
            modifier = Modifier
                .wrapContentHeight(),
            contentDescription = stringResource(R.string.etoile),
            tint = colorStar // TODO Denis : J'ai 2 fichiers de couleurs Color.kt et colors.xml : lequel utiliser ?
        )

        Spacer(modifier = Modifier.size(2.dp))

        Text(
            text = sNote,
            /*
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
            */
            /*.align(Alignment.CenterHorizontally)*/
            //style =  typo
        )

    }

}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES/*, showBackground = true*/)
@Composable
fun NoteComposablePreviewItem() {

    NoteComposable(
        modifier = Modifier
            .height(30.dp),
        sNote = "6.8"
    )

}


@Preview(name = "Light Mode")
@Composable
fun NoteComposablePreviewTablet() {

    NoteComposable(
        modifier = Modifier
            .height(60.dp),
        sNote = "5"
    )

}
