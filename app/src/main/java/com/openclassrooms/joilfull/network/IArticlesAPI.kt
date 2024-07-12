package com.openclassrooms.joilfull.network

import retrofit2.Response
import retrofit2.http.GET

interface IArticlesAPI {

    // Ex d'appel : https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/api/clothes.json


    /**
     * Renvoie une liste d'articles
     */
    @GET("clothes.json") // EndPoint
    suspend fun getArticles() : Response<List<APIResponseArticle>>


}