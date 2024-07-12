package com.openclassrooms.joilfull.model

class CategoryAndArticles(

    val sCategory : String,
    val listArticles : List<Article>

) {

    // Crée un
    companion object {

        /**
         * Il faut regrouper les articles par catégorie
         * et les mettre sous la forme d'une liste de CategoryAndArticles
         */
        fun createWithArticle(listArticles: List<Article>): List<CategoryAndArticles> {

            // Regrouper les articles par catégorie
            // Crée un Map<String,List<Article>>
            val listArticlesOrderByCategory = listArticles.groupBy {
                it.sCategory
            }

            // Créer une liste de CategoryAndArticles à partir du Map
            return listArticlesOrderByCategory.map {
                (category, articlesInCategory) ->
                CategoryAndArticles(category, articlesInCategory)
            }


        }
    }


}