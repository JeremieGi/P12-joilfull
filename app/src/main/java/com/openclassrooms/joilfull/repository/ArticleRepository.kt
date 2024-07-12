package com.openclassrooms.joilfull.repository

import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.network.IArticlesAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ArticleRepository(
    private val dataService: IArticlesAPI
)
{

    /**
     * Renvoie la liste des articles dans un Flow
     */
    fun loadArticlesList() : Flow<ResultCustom<List<Article>>> = flow {

        emit(ResultCustom.Loading)

        // Appel à l'API

        val responseRetrofit = dataService.getArticles()

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

}