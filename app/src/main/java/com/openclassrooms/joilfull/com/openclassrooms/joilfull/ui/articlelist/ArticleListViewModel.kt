package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joilfull.repository.ArticleRepository
import com.openclassrooms.joilfull.repository.ResultCustom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ArticleListViewModel  @Inject constructor(

    private val articleRepository : ArticleRepository

) : ViewModel() {


    // UI state - Chargement par défaut
    private val _uiState = MutableStateFlow<ArticleListUIState>(ArticleListUIState.IsLoading)
    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<ArticleListUIState> = _uiState.asStateFlow() // Accès en lecture seule de l'extérieur


    /**
     * Chargement des articles depuis le repository
     */
    fun loadArticlesList() {

        articleRepository.loadArticlesListSortByCategory().onEach { resultAPI ->

            // En fonction du résultat de l'API
            when (resultAPI) {

                // Echec
                is ResultCustom.Failure ->
                    _uiState.value = ArticleListUIState.Error(Exception(resultAPI.errorMessage))

                // En chargement
                ResultCustom.Loading -> {
                    _uiState.value = ArticleListUIState.IsLoading
                }

                // Succès
                is ResultCustom.Success -> {
                   _uiState.value = ArticleListUIState.Success(resultAPI.value)

                }

            }

        }.launchIn(viewModelScope)

    }



}