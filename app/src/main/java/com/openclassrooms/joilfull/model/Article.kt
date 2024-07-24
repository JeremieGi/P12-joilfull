package com.openclassrooms.joilfull.model

import android.content.Context
import com.openclassrooms.joilfull.R
import java.util.Locale

class Article (
    val nIDArticle : Int,
    val sURLPicture : String,
    val sDescriptionPicture : String,
    val sName : String,
    val sCategory : String, // Enumération ici : pas trop d'intéret si jamais le WS renvoie une nouvelle catégorie
    val nNbLikesInit : Int, // Nombre de like renvoyé par WS
    val dPrice : Double,
    val dOriginalPrice : Double

){

    // Cette donnée n'est pas récupération depuis le WS, j'ai donc créé une description
    val sDescriptionArticle = "$sName - Description non présente dans le WS : Pull vert forêt à motif torsadé élégant, tricot finement travaillé avec manches bouffantes et col montant; doux et chaleureux."

    // Permet de stocker en mémoire la liste des avis (notes et commentaires)
    // Pas de persistance de données
    private var _listArticleFeedback : MutableList<ArticleFeedback> = mutableListOf()

    // Permet de stocker en mémoire si l'article est favori de l'utilisateur courant
    // Le nombre de like étant renvoyé par l'appli, je stocke ici simplement en mémoire si l'utilisateur courant à ajouter l'article
    // Pas de persistance de données
    private var _bFavorite : Boolean = false
    val bFavorite: Boolean
        get() = _bFavorite


    /**
     * Chaine transmise à TalkBack pour annonce un item Article
     */
    fun sTalkBackSimpleDescription(context : Context): String {
        return context.getString(R.string.article_talkback, sName, getNbLikes().toString(), dPrice.toString(), dOriginalPrice.toString())
    }

    /**
     * Moyenne des notes de l'article
     */
    private fun dNotesAverage() : Double {

        // Extrait uniquement les notes
        val notesList: List<Int> = _listArticleFeedback.map { it.nNote }
        return notesList.average()

    }

    /**
     * Initialisation des feedbacks d'un article
     */
    fun initFeedback(articleFeedback: List<ArticleFeedback>) {
        this._listArticleFeedback = articleFeedback.toMutableList()
    }

    /**
     * Ajout d'un avis
     */
    fun addFeedback(articleFeedback: ArticleFeedback) {
        this._listArticleFeedback.add(articleFeedback)
    }

    /**
     * Note moyenne sous forme de chaine arrondie à 2 décimales
     */
    fun sAverageNote(context : Context): String {
        val number = dNotesAverage()
        if (number.isNaN()){ // NaN = Not a number
            // La moyenne ne peut pas être calculée (car pas de note)
            return context.getString(R.string.aucune)
        }
        else{
            val locale = Locale.getDefault()
            return String.format(locale, "%.2f", number)
        }

    }

    /**
     * Accesseur à _bFavorite
     */
    fun setFavorite(bFavoriteP : Boolean){
        this._bFavorite = bFavoriteP
    }

    /**
     *
     */
    fun getNbLikes() : Int {

        var nbLike = this.nNbLikesInit
        if (this._bFavorite) nbLike++
        return  nbLike

    }



}