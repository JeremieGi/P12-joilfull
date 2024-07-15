package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import androidx.lifecycle.ViewModel
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(

    private val articleRepository : ArticleRepository

) : ViewModel() {

    // UI state - Chargement par défaut
    private val _uiState = MutableStateFlow<ArticleUIState>(ArticleUIState.IsLoading)
    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<ArticleUIState> = _uiState.asStateFlow() // Accès en lecture seule de l'extérieur


    fun getArticleById(articleId: Int): Article {

        // TODO à écrire
        return Article(
            nIDArticle = 1,
            sURLPicture = "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/1.jpg",
            sDescriptionPicture = "Sac à main orange posé sur une poignée de porte",
            sName = "Code à enlever",
            sCategory = "ACCESSORIES", // Enumération ici : pas trop d'intéret si jamais le WS renvoie une nouvelle catégorie
            nNbLikes = 56,
            dPrice = 69.99,
            dOriginalPrice = 99.00,
            bFavorite = false)

    }


}