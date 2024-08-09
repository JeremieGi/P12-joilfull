package com.openclassrooms.joilfull

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

// Singleton pour stocker les chaines des deeps links
object Links {



    const val CTE_ROUTE = "article_item"            // Reprend la chaine définie dans le Manifest.xml => <data android:scheme="joiefull" android:host="article_item" />
    const val CTE_PARAM_ID_ARTICLE = "articleId"

    private const val CTE_DEEP_LINK = "joiefull://$CTE_ROUTE"

    val arguments = listOf(
        navArgument(CTE_PARAM_ID_ARTICLE) { type = NavType.StringType }
    )

    val deepLinks = listOf(
        navDeepLink { uriPattern = "$CTE_DEEP_LINK/{$CTE_PARAM_ID_ARTICLE}" }
    )

    const val CTE_ROUTE_WITH_ARGS = "${CTE_ROUTE}/{${CTE_PARAM_ID_ARTICLE}}"

    // Je n'arrive pas à tester le deep link 100% sur l'émulateur
    // Pour tester le deep link via adb : .\adb.exe shell am start -d "joiefull://article_item/2" -a android.intent.action.VIEW
    // Sur mon poste : C:\Users\User\AppData\Local\Android\Sdk\platform-tools\adb.exe
    // mais comment faire pour tester en réel ?
    // J'ai enregistré un fichier html sur l'émulateur et je l'ai lancé : <p>Cliquez sur ce lien pour voir l'article : <a href="joiefull://article_item/2">joiefull://article_item/2</a></p> => Ca fonctionne


    /**
     * Renvoie le deep link d'un article à partir de son ID
     */
    fun createDeepLink(nIDArticleP : Int?): String {

        return "${CTE_DEEP_LINK}/${nIDArticleP}" // joiefull://article_item/2

    }

}