package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

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
class ArticleViewModel @Inject constructor(

    private val articleRepository : ArticleRepository

) : ViewModel() {

    // UI state - Chargement par défaut
    private val _uiState = MutableStateFlow<ArticleUIState>(ArticleUIState.IsLoading)
    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<ArticleUIState> = _uiState.asStateFlow() // Accès en lecture seule de l'extérieur



    // Renvoie un article par son ID
    fun loadArticleByID(articleId: Int) {

        articleRepository.loadArticleByID(articleId).onEach { resultAPI ->

            // En fonction du résultat de l'API
            when (resultAPI) {

                // Echec
                is ResultCustom.Failure ->
                    _uiState.value = ArticleUIState.Error(Exception(resultAPI.errorMessage))

                // En chargement
                ResultCustom.Loading -> {
                    if (_uiState.value != ArticleUIState.IsLoading){
                        _uiState.value = ArticleUIState.IsLoading
                    }

                }

                // Succès
                is ResultCustom.Success -> {
                    _uiState.value = ArticleUIState.Success(resultAPI.value)

                }

            }

        }.launchIn(viewModelScope)

    }


}