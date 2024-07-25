package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val _uiState = MutableStateFlow<ArticleUIState>(ArticleUIState.IsLoadingArticle)
    // Backing property to avoid state updates from other classes
    val uiState: StateFlow<ArticleUIState> = _uiState.asStateFlow() // Accès en lecture seule de l'extérieur

    private var _nCurrentNote = mutableStateOf(0)
    val nCurrentNote: State<Int> = _nCurrentNote

    // Renvoie un article par son ID
    fun loadArticleByID(articleId: Int) {

        articleRepository.loadArticleByID(articleId).onEach { resultAPI ->

            // En fonction du résultat de l'API
            when (resultAPI) {

                // Echec
                is ResultCustom.Failure ->
                    _uiState.value = ArticleUIState.ErrorArticle(Exception(resultAPI.errorMessage))

                // En chargement
                is ResultCustom.Loading -> {
                     _uiState.value = ArticleUIState.IsLoadingArticle
                  }

                // Succès
                is ResultCustom.Success -> {

                    _uiState.value = ArticleUIState.ArticleSelected(resultAPI.value)

                }

                // NoneArticleSelected => pas traité ici

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
     * Récupère l'ID de l'utilisateur courant
     */
    fun getCurrentUserID() : Int {
        return userRepository.getCurrentUserID()
    }


    /**
     * Enregistrer la note et le commentaire saisi par l'utilisateur
     */
    fun sendNoteAndComment(nNoteP :Int , sCommentP : String) {

        getCurrentArticle()?.let { article ->

            val nIDCurrentUser = userRepository.getCurrentUserID()

            articleRepository.addFeedback(
                nIDArticleP = article.nIDArticle,
                nNoteP = nNoteP,
                sCommentP = sCommentP,
                nIDCurrentUser = nIDCurrentUser
            )

        }

    }

    /**
     * Article liké par l'utisateur courant
     */
    fun setLike(bValLikeP : Boolean){

        getCurrentArticle()?.let { article ->

            //val nIDCurrentUser = userRepository.getCurrentUserID()
            articleRepository.setLike(article.nIDArticle,bValLikeP)

            // Redéclenche un chargement de la fiche
            // TODO JG : Ne recharge pas l'article car il ne change pas..
            loadArticleByID(article.nIDArticle)

        }

    }

    /**
     * Désélectionne un article
     */
    fun unselectArticle(){

       // val currentState = _uiState.value

        //if (currentState is ArticleUIState.ArticleSelected) {
            _uiState.value = ArticleUIState.NoneArticleSelected
        //}


    }

    /**
     * Renvoie l'article sélectionné
     */
    private fun getCurrentArticle() : Article? {


        var articleResult : Article? = null

            if (uiState.value is ArticleUIState.ArticleSelected) {

                val successArticleState = uiState.value as ArticleUIState.ArticleSelected

                articleResult =successArticleState.article

            }


        return articleResult

    }

    fun updateNote(nNoteInputP : Int) {
        this._nCurrentNote.value = nNoteInputP
    }


}