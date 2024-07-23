package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.openclassrooms.joilfull.Links
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.CTE_MIN_SIZE
import com.openclassrooms.joilfull.model.Article

@Composable
fun ShareButtonComposable(
    modifier: Modifier = Modifier,
    articleP : Article,

    ){

    val currentContext = LocalContext.current

    Box(
        modifier = modifier
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp)
            ) // Fond avec coins arrondis
    ) {
        IconButton(
            onClick = {
                shareArticle(articleP.nIDArticle, currentContext)
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

}


/**
 * Partage l'article sur les réseaux sociaux
 */
fun shareArticle(nIDArticleP : Int, currentContext : Context) {


    val sDeepLink = Links.createDeepLink(nIDArticleP)

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, currentContext.getString(R.string.je_partage_cet_article, sDeepLink))
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