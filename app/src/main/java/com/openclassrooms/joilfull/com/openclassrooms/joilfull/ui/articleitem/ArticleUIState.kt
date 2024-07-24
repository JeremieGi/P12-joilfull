package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import com.openclassrooms.joilfull.model.Article

sealed class ArticleUIState {

    data object IsLoadingArticle : ArticleUIState()

    data class SuccessArticle(
        val article : Article,
    ) : ArticleUIState()




    data class ErrorArticle(val exception: Throwable) : ArticleUIState() // Error = sous-classe de ArticleListUIState

}
