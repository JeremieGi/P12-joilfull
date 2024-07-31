package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist

import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleUIState
import com.openclassrooms.joilfull.model.CategoryAndArticles


sealed class ArticleListUIState  {

    data object IsLoading : ArticleListUIState()

    data class Success(

        val categoryAndArticles : List<CategoryAndArticles>,

        val uiStateArticleSelect : ArticleUIState


    ) : ArticleListUIState()

    data class Error(val exception: Throwable) : ArticleListUIState() // Error = sous-classe de ArticleListUIState


}




