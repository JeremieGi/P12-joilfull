package com.openclassrooms.joilfull

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

// Singleton pour stocker les chaines des deeps links
object Links {



    const val CTE_ROUTE = "article_item" // Reprend la chaine défini dans le Manifest.xml => <data android:scheme="joiefull" android:host="article_item" />
    const val CTE_PARAM_ID_ARTICLE = "articleId"

    private const val CTE_DEEP_LINK = "joiefull://$CTE_ROUTE"

    val arguments = listOf(
        navArgument(CTE_PARAM_ID_ARTICLE) { type = NavType.StringType }
    )

    val deepLinks = listOf(
        navDeepLink { uriPattern = "$CTE_DEEP_LINK/{$CTE_PARAM_ID_ARTICLE}" }
    )

    val routeWithArgs = "${CTE_ROUTE}/{${CTE_PARAM_ID_ARTICLE}}"

    // TODO Denis : Je n'arrive pas à tester 100% sur l'émulateur
    // Pour tester le deep link via adb : .\adb.exe shell am start -d "joiefull://article_item/2" -a android.intent.action.VIEW

    fun createDeepLink(nIDArticleP : Int?): String {

        return "${CTE_DEEP_LINK}/${nIDArticleP}"

    }

}