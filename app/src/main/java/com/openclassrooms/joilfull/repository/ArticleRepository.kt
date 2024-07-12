package com.openclassrooms.joilfull.repository


import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.model.CategoryAndArticles
import com.openclassrooms.joilfull.network.IArticlesAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow


class ArticleRepository(
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


}