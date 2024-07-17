package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.ui.theme.JoilfullTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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

    LazyColumn (
  //      modifier = modifier
/*
            .background(
                color = Color.Blue
            )
  */
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
                modifier = Modifier.padding(
                    bottom = 30.dp   // Pour mettre un espace bien visible entre les catégories
                ),
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

    Column (
        modifier = modifier // Récupération du padding passé en paramètre du LazyColumn (appelant) -> espace en fin d'item
    ) {

        Text(
            modifier = Modifier.padding(
                vertical = 10.dp
            ),
            text = categoryAndArticles.sCategory,
            style = MaterialTheme.typography.titleLarge,


            //color = MaterialTheme.colorScheme.primary // Noir pour respecter les maquettes
        )

        LazyRow(
            //modifier = modifier,
            contentPadding = PaddingValues(horizontal = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),

        ) {
            items(
                items = categoryAndArticles.listArticles,
                key = { it.nIDArticle }
            ) { article ->
                ArticleItemComposable(
                    modifier = modifier
                        .size(width = 198.dp, height = 250.dp) // Taille définie dans l'appelant
                        .background(MaterialTheme.colorScheme.surfaceContainer),
                    article=article,
                    onArticleClickP=onArticleClickP
                )
            }
        }

    }

}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CategoryAndArticlesItemScreenPreview() {

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

