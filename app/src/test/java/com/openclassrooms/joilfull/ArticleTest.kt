package com.openclassrooms.joilfull

import com.openclassrooms.joilfull.model.Article
import com.openclassrooms.joilfull.model.ArticleFeedback
import org.junit.Assert.assertEquals
import org.junit.Test

class ArticleTest {

    @Test
    fun test_addFeedback(){

        val cutArticle = Article(
            nIDArticle = 0,
            sURLPicture = "",
            sDescriptionPicture = "Article0",
            sName = "Article0",
            sCategory = "ACCESSORIES",
            nNbLikesInit = 50,
            dPrice = 10.0,
            dOriginalPrice = 15.0
        )

        val feedback1 = ArticleFeedback(
            nIdArticle = 0,
            nNote = 1,
            sComment = "Comment",
            nIDUser = 1
        )

        // Ajout d'un avis
        cutArticle.addFeedback(feedback1)

        // L'avis est présent dans la liste d'avis
        assertEquals(1,cutArticle.getCountFeedback())

    }

    @Test
    fun test_getExistingNote(){

        val cutArticle = Article(
            nIDArticle = 0,
            sURLPicture = "",
            sDescriptionPicture = "Article0",
            sName = "Article0",
            sCategory = "ACCESSORIES",
            nNbLikesInit = 50,
            dPrice = 10.0,
            dOriginalPrice = 15.0
        )

        val feedback1 = ArticleFeedback(
            nIdArticle = 0,
            nNote = 1,
            sComment = "Comment",
            nIDUser = 1
        )

        cutArticle.addFeedback(feedback1)
        assertEquals(1,cutArticle.getCountFeedback())

        val feedback = cutArticle.getExistingNote(1)
        assertEquals("Comment",feedback?.sComment)

        val feedbackNotFound = cutArticle.getExistingNote(2)
        assertEquals(null,feedbackNotFound)


    }


    @Test
    fun test_LikeAndFavorite(){

        val cutArticle = Article(
            nIDArticle = 0,
            sURLPicture = "",
            sDescriptionPicture = "Article0",
            sName = "Article0",
            sCategory = "ACCESSORIES",
            nNbLikesInit = 50,
            dPrice = 10.0,
            dOriginalPrice = 15.0
        )


        assertEquals(50,cutArticle.getNbLikes())

        cutArticle.setFavorite(true)
        assertEquals(51,cutArticle.getNbLikes())

        cutArticle.setFavorite(false)
        assertEquals(50,cutArticle.getNbLikes())

    }

    @Test
    fun test_NoteAverage(){

        val cutArticle = Article(
            nIDArticle = 0,
            sURLPicture = "",
            sDescriptionPicture = "Article0",
            sName = "Article0",
            sCategory = "ACCESSORIES",
            nNbLikesInit = 50,
            dPrice = 10.0,
            dOriginalPrice = 15.0
        )

        val feedback1 = ArticleFeedback(
            nIdArticle = 0,
            nNote = 1,
            sComment = "Comment",
            nIDUser = 1
        )
        cutArticle.addFeedback(feedback1)

        val feedback2 = ArticleFeedback(
            nIdArticle = 1,
            nNote = 2,
            sComment = "Comment",
            nIDUser = 2
        )
        cutArticle.addFeedback(feedback2)

        val res = cutArticle.dNotesAverage()
        assertEquals(1.5, res, 0.1)


    }

    /**
     * Moyenne des otes d'un article sans note
     */
    @Test
    fun test_NoteAverage_WithoutNote(){

        val cutArticle = Article(
            nIDArticle = 0,
            sURLPicture = "",
            sDescriptionPicture = "Article0",
            sName = "Article0",
            sCategory = "ACCESSORIES",
            nNbLikesInit = 50,
            dPrice = 10.0,
            dOriginalPrice = 15.0
        )


        val res = cutArticle.dNotesAverage()
        assert(res.isNaN())

    }


}