package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme

/**
 * Ecran affichant une liste d'article
 */

@Composable
fun ArticleListScreen(
    listArticles : List<Article>,
    modifier: Modifier = Modifier
) {

    Column (modifier = modifier) {
        Text(
            text = "Liste des ${listArticles.size} articles à implémenter",
        )
    }


}



@Preview(
    showBackground = true
)
@Composable
fun ArticleListScreenPreview() {

    val listArticles : List<Article> = listOf(
        Article(
            nIDArticle = 0,
            sURLPicture = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/1.jpg",
            sDescriptionPicture = "Sac à main orange posé sur une poignée de porte",
            sName = "Sac à main orange",
            sCategory = "ACCESSORIES", // Enumération ici : pas trop d'intéret si jamais le WS renvoie une nouvelle catégorie
            nNbLikes = 56,
            dPrice = 69.99,
            dOriginalPrice = 99.00,
            bFavorite = false)
    )


    JoilfullTheme {
        ArticleListScreen(listArticles)
    }
}

