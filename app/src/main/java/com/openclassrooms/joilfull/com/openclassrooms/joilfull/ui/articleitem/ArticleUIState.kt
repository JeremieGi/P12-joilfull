package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import com.openclassrooms.joilfull.model.Article

sealed class ArticleUIState {

    data object IsLoadingArticle : ArticleUIState()

    // TODO : Je n'avais pas de changement d'état au chagment de valeur de  article
//    data class SuccessArticle(
//        val article : Article,
//    ) : ArticleUIState()

    // Article sélectionné
    data class ArticleSelected(val article: Article) : ArticleUIState()

    // Aucun article sélectionné
    data object NoneArticleSelected : ArticleUIState()

    data class ErrorArticle(val exception: Throwable) : ArticleUIState() // Error = sous-classe de ArticleListUIState

}
