package com.openclassrooms.joilfull.repository


import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.model.CategoryAndArticles
import com.openclassrooms.joilfull.network.IArticlesAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class ArticleRepository @Inject constructor (
    private val dataService: IArticlesAPI
)
{

    /**
     * Renvoie la liste des articles dans un Flow en appelant le WebService
     */
    fun loadArticlesList() : Flow<ResultCustom<List<Article>>> = flow {

        emit(ResultCustom.Loading)

        // Appel à l'API

        val responseRetrofit = dataService.getArticles()

        //delay(5000) // Pour tester le chargement
        //throw Exception("test exception") // Pour tester la gestion d'exception

        // si la requête met du temps, pas grave, on est dans une coroutine, le thread principal n'est pas bloqué

        if (responseRetrofit.isSuccessful){

            val listAPIResponseArticle = responseRetrofit.body()

            // transform in model object
            val resultListCandidate : List<Article>

            // Si le Ws renvoie aucune list => on renvoie une liste vide
            if (listAPIResponseArticle.isNullOrEmpty()){
                resultListCandidate = emptyList()
            }
            else{
                // On convertit en list modèle
                resultListCandidate = listAPIResponseArticle.map {
                    it.toModelArticle()
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

    /**
     * Renvoie un article par son ID (Normalement le WD devrait me proposer un point d'entrée pour faire çà)
     * @param : Identifiant de l'article
     */
    fun loadArticleByID(nIDArticle : Int)  : Flow<ResultCustom<Article>> = flow {

        // TODO : J'aurai aimé stocké le Flow en tant que propriété du repository (pour ne pas avoir à rappeler le Ws à chaque fois)
        // mais je n'arrive pas à réutiliser ce Flow dans cette méthode par exemple

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
                        it.nIDArticle == nIDArticle
                    }

                    // Normalement un seul article
                    if (listArticleFilter.size == 1){
                        emit(ResultCustom.Success(listArticleFilter[0]))
                    }
                    when (listArticleFilter.size){
                        1 ->
                            emit(ResultCustom.Success(listArticleFilter[0]))
                        0 ->
                            emit(ResultCustom.Failure("Article ID $nIDArticle non trouvé"))
                        else ->
                            emit(ResultCustom.Failure("Plusieurs articles ID $nIDArticle"))
                    }

                }


            }

        }

    }


}