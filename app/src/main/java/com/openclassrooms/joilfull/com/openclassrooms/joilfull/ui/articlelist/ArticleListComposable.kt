package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleItemComposable
import com.openclassrooms.joilfull.model.CategoryAndArticles

/**
 * Ecran affichant une liste d'article
 */

@Composable
fun ArticleListComposable(
    listCategoryAndArticles : List<CategoryAndArticles>,
    onArticleClickP : (Article) -> Unit,
    modifier: Modifier = Modifier
) {

    /*
    Text(
        modifier = modifier,
        text = "Liste des ${listCategoryAndArticles.size} categories à implémenter",
    )
    */
    LazyColumn (
        modifier = modifier

            .background(
                color = Color.Blue // TODO : A enlever
            )
    ) {

        /*
         * Le DSL de LazyListScope fournit un certain nombre de fonctions permettant de décrire les éléments de la mise en page.
         * En premier lieu, item() ajoute un seul élément,
         * et items(Int) ajoute plusieurs éléments :
         */
        items(
            items = listCategoryAndArticles,
            key = { it.sCategory }
        ) { categoryAndArticles ->
            CategoryAndArticlesItemScreen(
                modifier = modifier,
                onArticleClickP = onArticleClickP,
                categoryAndArticles = categoryAndArticles)
        }

    }


}

@Composable
fun CategoryAndArticlesItemScreen(
    modifier: Modifier = Modifier,
    onArticleClickP : (Article) -> Unit,
    categoryAndArticles : CategoryAndArticles
) {

    Column (modifier = modifier) {

        Text(
            text = categoryAndArticles.sCategory
        )

        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),

        ) {
            items(
                items = categoryAndArticles.listArticles,
                key = { it.nIDArticle }
            ) { article ->
                ArticleItemComposable(
                    modifier = modifier,
                        //.height(75.dp),
                    article=article,
                    onArticleClickP=onArticleClickP
                )
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

    val categ = CategoryAndArticles(
        sCategory = "Catégorie preview",
        listArticles = listArticlesMut.toList()
    )


    JoilfullTheme {
        CategoryAndArticlesItemScreen(
            categoryAndArticles = categ,
            onArticleClickP = {}
        )
    }
}

