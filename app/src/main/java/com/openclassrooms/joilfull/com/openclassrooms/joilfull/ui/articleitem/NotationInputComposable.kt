package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.joilfull.R

@Composable
fun NotationInputComposable(
    modifier: Modifier = Modifier,
    fInitialNote : Float,
    nIDUser : Int,
    nIDArticle : Int
){

    Column(
        modifier = modifier
    )
    {

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.currentuseravatar),
                contentDescription = stringResource(R.string.votre_avatar_utilisateur),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(percent = 100))
            )

            // Champ de notation à 5 étoiles
            StarRatingBar(
                modifier = Modifier.wrapContentWidth(),
                rating = fInitialNote,
                onRatingChanged = {}
            )
        }

        Spacer(modifier = Modifier
            .height(16.dp)
            .width(15.dp)
        )

        // Deuxième ligne : Champ de saisie de texte
        var text by remember { mutableStateOf("") }
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(16.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
        )


    }


}


@Composable
fun StarRatingBar(
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    rating: Float,
    onRatingChanged: (Float) -> Unit
) {
    val density = LocalDensity.current.density
    val starSize = (12f * density).dp
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.Star

            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color(0x20FFFFFF)

            Icon(
                modifier = Modifier
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onRatingChanged(i.toFloat())
                        }
                    )
                    .width(starSize).height(starSize),
                imageVector = icon,
                contentDescription = stringResource(R.string.partager),
                tint = iconTintColor
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES/*, showBackground = true*/)
@Composable
fun NotationInputComposablePreview() {

    NotationInputComposable(
        modifier = Modifier.width(
            width = 200.dp
        ),
        nIDUser = 1,
        nIDArticle = 1,
        fInitialNote = 3f
    )

}