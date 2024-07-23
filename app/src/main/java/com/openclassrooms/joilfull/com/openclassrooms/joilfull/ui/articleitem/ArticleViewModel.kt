package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joilfull.Links
import com.openclassrooms.joilfull.R
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist.ArticleListUIState
import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.repository.ArticleRepository
import com.openclassrooms.joilfull.repository.ResultCustom
import com.openclassrooms.joilfull.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(

    private val articleRepository : ArticleRepository,
    private val userRepository : UserRepository

) : ViewModel() {

    // UI state - Chargement par défaut
    private val _uiState = MutableStateFlow<ArticleUIState>(ArticleUIState.NoSelectedArticle)
    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<ArticleUIState> = _uiState.asStateFlow() // Accès en lecture seule de l'extérieur

    // Par simplicité de gestion, je stocke l'article chargé
    private var _currentArticle : Article? = null
    val currentArticle: Article?
        get() = _currentArticle


    // Renvoie un article par son ID
    fun loadArticleByID(articleId: Int) {

        articleRepository.loadArticleByID(articleId).onEach { resultAPI ->

            // En fonction du résultat de l'API
            when (resultAPI) {

                // Echec
                is ResultCustom.Failure ->
                    _uiState.value = ArticleUIState.Error(Exception(resultAPI.errorMessage))

                // En chargement
                is ResultCustom.Loading -> {
                    if (_uiState.value != ArticleUIState.IsLoading){
                        _uiState.value = ArticleUIState.IsLoading
                    }

                }

                // Succès
                is ResultCustom.Success -> {

                    // On conserve l'article chargé dans le viewModel
                    _currentArticle = resultAPI.value

                    _uiState.value = ArticleUIState.Success(resultAPI.value)

                }

            }

        }.launchIn(viewModelScope)

    }

    /**
     * Récupère l'avatar de l'utilisateur courant
     */
    fun getCurrentUserAvatar() : Int {
        return userRepository.getCurrentUserAvatar()
    }

    /**
     * Enregistrer la note et le commentaire saisi par l'utilisateur
     */
    fun sendNoteAndComment(fNote:Float , sComment : String) {

        _currentArticle.let {

            val nIDCurrentUser = userRepository.getCurrentUserID()

        }


    }

    /**
     * Article liké par l'utisateur courant
     */
    fun setLike(bValLikeP : Boolean){

        _currentArticle.let {

            val nIDCurrentUser = userRepository.getCurrentUserID()
            // TODO JG Où enregistrer les likes ?

        }

    }



}