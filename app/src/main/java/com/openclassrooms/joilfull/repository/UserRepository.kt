package com.openclassrooms.joilfull.repository

import com.openclassrooms.joilfull.R
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Cette classe simule l'accès à un repository dédié aux utilisateurs
 */
@Singleton
class UserRepository  @Inject constructor () {


    /**
     * Renvoie l'ID de l'utilisateur courant
     *
     */
    fun getCurrentUserID() : Int {
        return 1
    }

    /**
     * Renvoie l'avatar de l'utilisateur courant
     */
    fun getCurrentUserAvatar() : Int {
        return R.drawable.currentuseravatar
    }


}