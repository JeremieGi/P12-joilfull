package com.openclassrooms.joilfull.network

import com.openclassrooms.joilfull.model.ArticleFeedback
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simulation d'une API qui renverra une liste de notes et commentaires par article
 */
@Singleton
class FakeAPIFeedback @Inject constructor() {

    private var _listFeedback : MutableList<ArticleFeedback> = mutableListOf()
//    // Public read-only property
//    val listFeedback: List<ArticleFeedback>
//        get() = _listFeedback

    init {
        initFakeAPI()
    }

    private fun initFakeAPI() {

        // Article 0 -> Pas d'avis

        // Article 1 -> 1 Avis
        _listFeedback.add(
            ArticleFeedback(
                1,
                4,
                "Super article",
                nIDUser = 1
                )
        )

        // Article 2 -> 2 Avis
        _listFeedback.add(
            ArticleFeedback(
                2,
                4,
                "Super article",
                nIDUser = 1
            )
        )
        _listFeedback.add(
            ArticleFeedback(
                2,
                2,
                "Article pas top",
                nIDUser = 2
            )
        )

        // Pour le reste algo qui crée des avis
        for (nIDArticle in 3..11){

            // nombre d'avis = à l'id de l'article
            for (nNbFeedback in 1..nIDArticle){

                _listFeedback.add(
                    ArticleFeedback(
                        nIDArticle,
                        (nNbFeedback % 5) +1,
                        "Commentaire numéro ${nIDArticle+nNbFeedback}",
                        nIDUser = nNbFeedback
                    )
                )

            }

        }

    }

    /**
     * Renvoie les avis de l'article passé en paramètre
     */
    fun getArticleFeedback(nIDArticleP : Int) : List<ArticleFeedback>{

        return _listFeedback.filter { it.nIdArticle == nIDArticleP }

    }


}