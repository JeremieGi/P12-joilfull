package com.openclassrooms.joilfull.repository


import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.model.ArticleFeedback
import com.openclassrooms.joilfull.model.CategoryAndArticles
import com.openclassrooms.joilfull.network.FakeAPIFeedback
import com.openclassrooms.joilfull.network.IArticlesAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor (
    private val dataService: IArticlesAPI,
    private val fakeAPIFeedback: FakeAPIFeedback
)
{

    // Tableau associatif d'article indicé par ID
    private val _mapArticles: MutableMap<Int, Article> = mutableMapOf()

    /**
     * Renvoie la liste des articles dans un Flow en appelant le WebService
     */
    private fun loadArticlesList() : Flow<ResultCustom<List<Article>>> = flow {

        emit(ResultCustom.Loading)

        // Appel à l'API

        val responseRetrofit = dataService.getArticles()

        //delay(5000)                           // Pour tester le chargement
        //throw Exception("test exception")     // Pour tester la gestion d'exception

        // si la requête met du temps, pas grave, on est dans une coroutine, le thread principal n'est pas bloqué

        if (responseRetrofit.isSuccessful){

            val listAPIResponseArticle = responseRetrofit.body()

            val resultListCandidate : List<Article>

            // Réinitialise la liste du répository
            _mapArticles.clear()

            // Si le WS renvoie aucune list => on renvoie une liste vide
            if (listAPIResponseArticle.isNullOrEmpty()){
                resultListCandidate = emptyList()
            }
            else{

                // On convertit en list modèle (List<Article>)
                resultListCandidate = listAPIResponseArticle
                    .map {
                        it.toModelArticle()
                    }

                // On charge aussi les feedbacks
                resultListCandidate.forEach {
                    // TODO Denis Question : si le chargement de fakeAPIFeedback était asynchrone (un autre WS par exemple) çà marcherait ?
                    it.initFeedback(fakeAPIFeedback.getArticleFeedback(it.nIDArticle))
                }

                // Remplissage du Map du repository
                resultListCandidate.forEach {
                    _mapArticles[it.nIDArticle] = it
                }


            }

            // Ajout au flow
            emit(ResultCustom.Success(resultListCandidate))

        }
        else{
            emit(ResultCustom.Failure("Reponse HTTP avec code erreur ${responseRetrofit.code()}" +
                    " : ${responseRetrofit.message()} "))
        }


    }.catch { error ->

        emit(ResultCustom.Failure(error.message+" "+error.cause?.message))

    }


    /**
     * Renvoie la liste des articles classés par catégorie
     */
    fun loadArticlesListSortByCategory()  : Flow<ResultCustom<List<CategoryAndArticles>>> = flow {

        // Si la liste d'articles du répository est vide
        if (_mapArticles.isEmpty()){

            // Appel au WebService
            // Transforme le flow renvoyer par loadArticlesList()
            loadArticlesList().collect { resultAPI ->

                // En fonction du résultat de l'API
                when (resultAPI) {

                    // Echec
                    is ResultCustom.Failure ->
                        // Propagation du message d'erreur
                        emit(ResultCustom.Failure(resultAPI.errorMessage))

                    // En chargement
                    ResultCustom.Loading -> {
                        // Propagation du chargement
                        emit(ResultCustom.Loading)
                    }

                    // Succès
                    is ResultCustom.Success -> {

                        val listArticles = resultAPI.value

                        // Il faut regrouper les articles par catégorie et les mettre sous la forme d'une liste de CategoryAndArticles
                        val listArticlesByCategory = CategoryAndArticles.createWithArticle(listArticles)

                        emit(ResultCustom.Success(listArticlesByCategory))

                    }


                }

            }


        }
        else{

            // On renvoie la liste en mémoire

            // Il faut regrouper les articles par catégorie et les mettre sous la forme d'une liste de CategoryAndArticles
            val listArticlesByCategory = CategoryAndArticles.createWithArticle(_mapArticles.values.toList())
            emit(ResultCustom.Success(listArticlesByCategory))

        }




    }

    /**
     * Renvoie un article par son ID (Normalement le WD devrait me proposer un point d'entrée pour faire çà)
     * @param : Identifiant de l'article
     */
    fun loadArticleByID(nIDArticleP : Int)  : Flow<ResultCustom<Article>> = flow {


        // Si la liste d'articles du répository est vide
        if (_mapArticles.isEmpty()){

            loadArticlesList().collect { resultAPI ->

                // En fonction du résultat de l'API
                when (resultAPI) {

                    // Echec
                    is ResultCustom.Failure ->
                        // Propagation du message d'erreur
                        emit(ResultCustom.Failure(resultAPI.errorMessage))

                    // En chargement
                    ResultCustom.Loading -> {
                        // Propagation du chargement
                        emit(ResultCustom.Loading)
                    }

                    // Succès
                    is ResultCustom.Success -> {

                        // Tous les articles
                        val listArticles = resultAPI.value

                        // Recherche de l'article avec l'ID passé en paramètre
                        val listArticleFilter = listArticles.filter {
                            it.nIDArticle == nIDArticleP
                        }


                        when (listArticleFilter.size){
                            1 ->
                                // Normalement un seul article
                                emit(ResultCustom.Success(listArticleFilter[0]))
                            0 ->
                                emit(ResultCustom.Failure("Article ID $nIDArticleP non trouvé"))
                            else ->
                                emit(ResultCustom.Failure("Plusieurs articles ID $nIDArticleP"))
                        }

                    }


                }

            }

        }
        else{

            _mapArticles[nIDArticleP]?.let { article ->
                emit(ResultCustom.Success(article))
            } ?: run {
                // Gérer le cas où l'article n'est pas trouvé
                // Par exemple, émettre un échec ou une valeur par défaut
            }

        }



    }

    /**
     * Ajout une note et un commentaire à un article
     */
    fun addFeedback(nIDArticleP : Int, nNoteP : Int, sCommentP : String, nIDCurrentUser: Int){

        _mapArticles[nIDArticleP]?.addFeedback(
            ArticleFeedback(
                nIdArticle = nIDArticleP,
                nNote = nNoteP,
                sComment = sCommentP,
                nIDUser = nIDCurrentUser
            )
        )
    }

    /**
     *  Ajoute / Retire un article des favoris pour l'article courant
     */
    fun setLike(nIDArticleP: Int, bValLikeP: Boolean) {

        _mapArticles[nIDArticleP]?.setFavorite(bValLikeP)

    }


}