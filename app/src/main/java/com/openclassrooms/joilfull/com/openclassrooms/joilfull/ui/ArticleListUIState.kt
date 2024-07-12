package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui

import com.openclassrooms.joilfull.model.Article



sealed class ArticleListUIState  {

    //val selectedArticle : Article? = null

    data object IsLoading : ArticleListUIState()

    data class Success(
        val articles: List<Article>
    ) : ArticleListUIState()

    data class Error(val exception: Throwable) : ArticleListUIState() // Error = sous-classe de ArticleListUIState


}




