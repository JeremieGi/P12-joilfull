package com.openclassrooms.joilfull.model

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

)