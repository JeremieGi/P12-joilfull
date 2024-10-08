package com.openclassrooms.joilfull.repository

/**
 * Custom class to manage the different result
 */
sealed class ResultCustom<out T> {

    data object Loading : ResultCustom<Nothing>()

    // C'est une classe de données qui représente l'état où l'opération a échoué. Elle peut contenir un message décrivant l'erreur survenue.
    data class Failure(
        val errorMessage: String? = null,
    ) : ResultCustom<Nothing>()

    // C'est une classe de données générique qui stocke le résultat de l'opération en cas de succès.
    // Elle prend un type générique R pour permettre de représenter différents types de résultats.
    data class Success<out R>(
        val value: R // Permet de récupérer les valeurs de ResultBankAPI
    ) : ResultCustom<R>()

}