package com.openclassrooms.joilfull.network

import com.openclassrooms.joilfull.model.Article
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Exemple de retour :
 * [
 *   {
 *     "id": 0,
 *     "picture": {
 *       "url": "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/accessories/1.jpg",
 *       "description": "Sac à main orange posé sur une poignée de porte"
 *     },
 *     "name": "Sac à main orange",
 *     "category": "ACCESSORIES",
 *     "likes": 56,
 *     "price": 69.99,
 *     "original_price": 69.99
 *   },
 *   {
 *     "id": 1,
 *     "picture": {
 *       "url": "https://raw.githubusercontent.com/OpenClassrooms-Student-Center/D-velopper-une-interface-accessible-en-Jetpack-Compose/main/img/bottoms/1.jpg",
 *       "description": "Modèle femme qui porte un jean et un haut jaune"
 *     },
 *     "name": "Jean pour femme",
 *     "category": "BOTTOMS",
 *     "likes": 55,
 *     "price": 49.99,
 *     "original_price": 59.99
 *   }
 * ]
 *
 */

@JsonClass(generateAdapter = true)
data class Picture(
    @Json(name = "url") val url: String,
    @Json(name = "description") val description: String
)


@JsonClass(generateAdapter = true)
data class APIResponseArticle(
    @Json(name = "id")
    val id: Int,

    @Json(name = "picture")
    val picture: Picture,

    @Json(name = "name")
    val name: String,

    @Json(name = "category")
    val category: String,

    @Json(name = "likes")
    val likes: Int,

    @Json(name = "price")
    val price: Double,

    @Json(name = "original_price")
    val originalPrice: Double
){

    /**
     * Convert APi response in model object
     */
    fun toModelArticle() : Article {

        return Article(
            nIDArticle = this.id,
            sURLPicture = this.picture.url,
            sDescriptionPicture = this.picture.description,
            sName = this.name,
            sCategory = this.category,
            nNbLikes = this.likes,
            dPrice = this.price,
            dOriginalPrice = this.originalPrice
        )

    }

}

