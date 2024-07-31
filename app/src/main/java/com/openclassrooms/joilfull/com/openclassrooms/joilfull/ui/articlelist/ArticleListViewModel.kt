package com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articlelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.joilfull.com.openclassrooms.joilfull.ui.articleitem.ArticleUIState
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
class ArticleListViewModel  @Inject constructor(

    private val articleRepository : ArticleRepository,
    private val userRepository : UserRepository

) : ViewModel() {


    // UI state - Chargement par défaut
    private val _uiState = MutableStateFlow<ArticleListUIState>(ArticleListUIState.IsLoading)
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

                    // Y avait-il un article préalablement sélectionné ?
                    val selectedArticle = getSelectedArticle()
                    if (selectedArticle==null){
                        // Pas de sélection
                        _uiState.value = ArticleListUIState.Success(
                            categoryAndArticles = resultAPI.value,
                            uiStateArticleSelect = ArticleUIState.NoneArticleSelected
                        )
                    }
                    else{

                        // Article à sélectionner
                        val uiArticleState = ArticleUIState.ArticleSelected(article = selectedArticle)

                        _uiState.value = ArticleListUIState.Success(
                            categoryAndArticles = resultAPI.value,
                            uiStateArticleSelect = uiArticleState
                        )
                    }



                }

            }

        }.launchIn(viewModelScope)

    }


    // Renvoie un article par son ID
    fun loadArticleByID(articleId: Int) {

        val successState = _uiState.value as ArticleListUIState.Success

        articleRepository.loadArticleByID(articleId).onEach { resultAPI ->

            // En fonction du résultat de l'API
            when (resultAPI) {

                // Echec
                is ResultCustom.Failure ->
                    _uiState.value = ArticleListUIState.Success(
                        categoryAndArticles = successState.categoryAndArticles,
                        uiStateArticleSelect = ArticleUIState.ErrorArticle(Exception(resultAPI.errorMessage))
                    )

                // En chargement
                is ResultCustom.Loading -> {
                    _uiState.value = ArticleListUIState.Success(
                        categoryAndArticles = successState.categoryAndArticles,
                        uiStateArticleSelect = ArticleUIState.IsLoadingArticle
                    )
                }

                // Succès
                is ResultCustom.Success -> {

                    //setUnselectArticle()

                    val selectedArticle = resultAPI.value

                    // On change juste l'article sélectionné
                    _uiState.value = ArticleListUIState.Success(

                        categoryAndArticles = successState.categoryAndArticles,
                        uiStateArticleSelect = ArticleUIState.ArticleSelected(
                            article = selectedArticle
                        )

                    )



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
     * Récupère l'ID de l'utilisateur courant
     */
    fun getCurrentUserID() : Int {
        return userRepository.getCurrentUserID()
    }

    /**
     * Enregistrer la note et le commentaire saisi par l'utilisateur
     */
    fun sendNoteAndComment(nNoteP :Int , sCommentP : String) {

        val selectedArticle = getSelectedArticle()

        selectedArticle?.let { article ->

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

        val selectedArticle = getSelectedArticle()

        selectedArticle?.let { article ->

            //val nIDCurrentUser = userRepository.getCurrentUserID()
            articleRepository.setLike(article.nIDArticle,bValLikeP)

            // Redéclenche un chargement de toute la liste pour la mettre à jour
            loadArticlesList()

        }

    }

    /**
     * Renvoie l'article sélectionné et null si il n'y en a pas
     */
    fun getSelectedArticle() : Article? {

        var articleResult : Article? = null

        if (_uiState.value is ArticleListUIState.Success){

            val successListState = _uiState.value as ArticleListUIState.Success

            if (successListState.uiStateArticleSelect is ArticleUIState.ArticleSelected) {

                val successArticleState = successListState.uiStateArticleSelect

                articleResult =successArticleState.article

            }

        }

        return articleResult

    }

    /**
     * Désélectionne un article
     */
    fun setUnselectArticle(){

        val successState = _uiState.value as ArticleListUIState.Success

        _uiState.value = ArticleListUIState.Success(
            categoryAndArticles = successState.categoryAndArticles,
            uiStateArticleSelect = ArticleUIState.NoneArticleSelected
        )


    }


}