package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import com.openclassrooms.joilfull.model.Article

// TODO Denis : question : utilité du sealed dans l'archi ? (un open marcherait)
sealed class ArticleUIState {

    // Utile uniquement en mode tablette
    // Permet de définir un état où aucun article n'est sélectionné
    data object NoSelectedArticle : ArticleUIState()

    data object IsLoading : ArticleUIState()

    data class Success(
        val article : Article
    ) : ArticleUIState()

    data class Error(val exception: Throwable) : ArticleUIState() // Error = sous-classe de ArticleListUIState

}
