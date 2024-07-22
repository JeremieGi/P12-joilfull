package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.app.Activity
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.twotone.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.ui.theme.colorStar

@Composable
fun NotationInputComposable(
    modifier: Modifier = Modifier,
    nIDUser : Int,
    nIDArticle : Int,
    nIDRessourceAvatar : Int,
    onClickSendNote : (fNote:Float , sComment : String) -> Unit
){

    val context = LocalContext.current
    //val activity = context as? Activity

    Column(
        modifier = modifier
    )
    {

        var rating by rememberSaveable { mutableFloatStateOf(0f) }

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = nIDRessourceAvatar),
                contentDescription = stringResource(R.string.votre_avatar_utilisateur),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(percent = 100))
            )

            Spacer(modifier = Modifier
                .size(5.dp)
            )

            // Champ de notation à 5 étoiles
            StarRatingBar(
                modifier = Modifier.weight(1f),
                ratingP = rating,
                onRatingChanged = {
                    rating = it
                }
            )
        }

        Spacer(modifier = Modifier
            .height(10.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){

            // Champ de saisie de texte qui conserve sa valeur lors de la rotation
            var textComment by rememberSaveable { mutableStateOf("") }
            TextField (
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(5.dp)
                    .defaultMinSize(
                        minHeight = 50.dp
                    ),
                value = textComment,
                onValueChange = { textComment = it },
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                maxLines = 2,
                placeholder = {
                    Text(stringResource(R.string.note_placeholder))
                }
            )

            IconButton(
                onClick = {

                    var bInputOK = true

                    if (rating==0f){
                        Toast.makeText(context,
                            context.getString(R.string.merci_de_saisir_une_note), Toast.LENGTH_LONG).show()
                        bInputOK = false
                    }

                    if (textComment.isNullOrBlank()){
                        Toast.makeText(context,
                            context.getString(R.string.merci_de_saisir_un_commentaire), Toast.LENGTH_LONG).show()
                        bInputOK = false
                    }

                    if (bInputOK){
                        onClickSendNote(
                            /*fNote = */rating,
                            /*sComment = */textComment
                        )
                    }


                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.TwoTone.Send,
                    contentDescription = stringResource(R.string.envoyer_le_commentaire_et_la_note)
                )
            }

        }




    }


}



@Composable
fun StarRatingBar(
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    ratingP: Float,
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

            val isSelected = i <= ratingP

            val icon : ImageVector
            val iconTintColor : Color

            if (isSelected) {
                icon = Icons.TwoTone.Star
                iconTintColor = colorStar
            } else {
                icon = Icons.TwoTone.Star
                iconTintColor = MaterialTheme.colorScheme.secondary
            }

            Icon(
                modifier = Modifier
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onRatingChanged(i.toFloat())
                        }
                    )
                    .width(starSize)
                    .height(starSize),
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
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun NotationInputComposablePreview() {

    NotationInputComposable(
        modifier = Modifier
            .width(
                width = 300.dp
            )
           // .background(Color.Black /*MaterialTheme.colorScheme.surfaceDim*/)
            ,
        nIDUser = 1,
        nIDArticle = 1,
        nIDRessourceAvatar = R.drawable.currentuseravatar,
        onClickSendNote = { _, _ -> } // 2 paramètres vides
    )

}

@Preview(name = "Light Mode")
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun StarRatingBarPreview() {

    StarRatingBar(
        modifier = Modifier
            .width(
                width = 300.dp
            )
        ,
            //.background(Color.Black),
        maxStars = 5,
        ratingP = 3.5f,
        onRatingChanged = {}
    )

}

