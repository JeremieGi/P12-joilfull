package com.openclassrooms.joilfull

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

// Singleton pour stocker les chaines des deeps links
object Links {



    const val route = "article_item" // Reprend la chaine défini dans le Manifest.xml => <data android:scheme="joiefull" android:host="article_item" />
    const val articleIdArg = "articleId"

    val deepLinkRoute = "joiefull://$route"

    val arguments = listOf(
        navArgument(articleIdArg) { type = NavType.StringType }
    )

    val deepLinks = listOf(
        navDeepLink { uriPattern = "$deepLinkRoute/{$articleIdArg}" }
    )

    val routeWithArgs = "${route}/{${articleIdArg}}"

    // TODO Denis : Je n'arrive pas à tester 100% sur l'émulateur
    // Pour tester le deep link via adb : .\adb.exe shell am start -d "joiefull://article_item/2" -a android.intent.action.VIEW

    fun createDeepLink(nIDArticleP : Int?): Any {

        return "${deepLinkRoute}/${nIDArticleP}"

    }

}