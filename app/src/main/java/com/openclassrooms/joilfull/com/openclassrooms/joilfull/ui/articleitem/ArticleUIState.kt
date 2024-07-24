package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import com.openclassrooms.joilfull.model.Article

sealed class ArticleUIState {

    // Utile uniquement en mode tablette
    // Permet de définir un état où aucun article n'est sélectionné
    // Si je ne fais pas cet état, en mode tablette, le UiState ne change pas d'état lors du clic sur un article
    data object NoSelectedArticle : ArticleUIState()

    data object IsLoading : ArticleUIState()

    data class Success(
        val article : Article
    ) : ArticleUIState()

    data class Error(val exception: Throwable) : ArticleUIState() // Error = sous-classe de ArticleListUIState

}
