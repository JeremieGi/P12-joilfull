package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import com.openclassrooms.joilfull.model.Article

// TODO revoir l'utilité du sealed dans l'archi (un open marcherait)
sealed class ArticleUIState {


    data object IsLoading : ArticleUIState()

    data class Success(
        val article : Article
    ) : ArticleUIState()

    data class Error(val exception: Throwable) : ArticleUIState() // Error = sous-classe de ArticleListUIState

}
