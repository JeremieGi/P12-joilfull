package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import androidx.compose.foundation.lazy.items

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

        LazyRow(
            modifier = modifier
        ) {
            items(
                items = listArticles,
                key = { it.nIDArticle }
            ) { article ->
                ArticleItemScreen(article)
            }
        }

    }


}



@Preview(
    showBackground = true
)
@Composable
fun ArticleListScreenPreview() {

    val listArticlesMut = mutableListOf<Article>()
    for (i in 1..5) {
        val art = Article(
            nIDArticle = i,
            sURLPicture = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/1.jpg",
            sDescriptionPicture = "Sac à main orange posé sur une poignée de porte",
            sName = "Sac$i à main orange",
            sCategory = "ACCESSORIES", // Enumération ici : pas trop d'intéret si jamais le WS renvoie une nouvelle catégorie
            nNbLikes = 56+i,
            dPrice = 69.99+i,
            dOriginalPrice = 99.00,
            bFavorite = false)
        listArticlesMut.add(art)
    }


    JoilfullTheme {
        ArticleListScreen(listArticlesMut.toList())
    }
}

