package com.openclassrooms.joilfull.model

data class ArticleFeedback (
    val nIdArticle : Int,
    val nNote : Int,
    val sComment : String,
    val nIDUser : Int
)