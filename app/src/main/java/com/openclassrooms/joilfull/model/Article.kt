package com.openclassrooms.joilfull.model

import android.content.Context
import com.openclassrooms.joilfull.R

class Article (
    val nIDArticle : Int,
    val sURLPicture : String,
    val sDescriptionPicture : String,
    val sName : String,
    val sCategory : String, // Enumération ici : pas trop d'intéret si jamais le WS renvoie une nouvelle catégorie
    val nNbLikes : Int,
    val dPrice : Double,
    val dOriginalPrice : Double,
    val bFavorite : Boolean

){

    // TODO JG : Où je récupère cette donnée ?
    val sDescriptionArticle = "$sName - Description non présente dans le WS : Pull vert forêt à motif torsadé élégant, tricot finement travaillé avec manches bouffantes et col montant; doux et chaleureux."

    fun sTalkBackSimpleDescription(context : Context): String {
        // TODO JG : Ajouter la notation
        return context.getString(R.string.article_talkback, sName, nNbLikes.toString(), dPrice.toString(), dOriginalPrice.toString())
    }

}