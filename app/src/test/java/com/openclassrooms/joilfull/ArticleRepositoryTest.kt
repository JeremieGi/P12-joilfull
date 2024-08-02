package com.openclassrooms.joilfull

import android.util.Log
import com.openclassrooms.joilfull.model.ArticleFeedback
import com.openclassrooms.joilfull.network.APIResponseArticle
import com.openclassrooms.joilfull.network.FakeAPIFeedback
import com.openclassrooms.joilfull.network.IArticlesAPI
import com.openclassrooms.joilfull.network.Picture
import com.openclassrooms.joilfull.repository.ArticleRepository
import com.openclassrooms.joilfull.repository.ResultCustom
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ArticleRepositoryTest {

    private lateinit var cutArticleRepository : ArticleRepository //Class Under Test
    private lateinit var mockDataServiceAPI : IArticlesAPI
    private lateinit var mockFakeAPIFeedback: FakeAPIFeedback

    private lateinit var mockDataServiceAPIResponse : List<APIResponseArticle>
    private lateinit var mockFakeAPIFeedbackResponse : List<ArticleFeedback>

    /**
     * Create mock object
     */
    @Before
    fun createRepositoryWithMockedDao() {

        mockDataServiceAPI = mockk()
        mockFakeAPIFeedback = mockk()
        cutArticleRepository = ArticleRepository(mockDataServiceAPI,mockFakeAPIFeedback)

        // Mock the Log class
        // Log utilise le framework Android et ne peut pas être appelé dans un test unitaire
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0


        mockDataServiceAPIResponse = listOf(
            APIResponseArticle(
                id=0,
                picture = Picture("url","description"),
                name="Article0",
                category = "categoryTest",
                likes = 0,
                price= 10.0,
                originalPrice = 20.0
            ),
            APIResponseArticle(
                id=1,
                picture = Picture("url","description"),
                name="Article1",
                category = "categoryTest",
                likes = 1,
                price= 11.0,
                originalPrice = 21.0
            ),
            APIResponseArticle(
                id=2,
                picture = Picture("url","description"),
                name="Article2",
                category = "categoryTest2",
                likes = 2,
                price= 12.0,
                originalPrice = 22.0
            )
        )

        mockFakeAPIFeedbackResponse = listOf(
            ArticleFeedback(
                nIdArticle=0,
                nNote=5,
                sComment="Commentaire1",
                nIDUser=1
            ),
            ArticleFeedback(
                nIdArticle=0,
                nNote=4,
                sComment="Commentaire2",
                nIDUser=2
            )
        )


    }



    /**
     * loadArticlesListSortByCategory
     */
    @Test
    fun test_loadArticlesListSortByCategory() = runTest {

        // MOCK
        //val listArticle = List<Article>


        coEvery {
            mockDataServiceAPI.getArticles()
        } returns Response.success(mockDataServiceAPIResponse)


        coEvery {
            mockFakeAPIFeedback.getArticleFeedback(any())
        } returns mockFakeAPIFeedbackResponse


        //when => Test réel de la fonction
        val listResult = run {
            cutArticleRepository.loadArticlesListSortByCategory().toList()
        }

        coVerify {
            mockDataServiceAPI.getArticles()
            mockFakeAPIFeedback.getArticleFeedback(any())
        }

        // On attend 2 valeurs dans le flow
        assertEquals(2, listResult.size)

        // Première valeur => Loading
        assertEquals(ResultCustom.Loading, listResult[0])

        // Deuxième valeur => La réponse avec succès
        val res = listResult[1]

        assert(res is ResultCustom.Success)

        if (res is ResultCustom.Success){

            val listArticlesByCategory = res.value
            assertEquals(listArticlesByCategory.size,2) // 3 articles dans 2 catégories différentes

        }
        else{
            assertFalse("Le résultat devrait être de type ResultCustom.Success",false)
        }

    }


    /**
     * Simule une erreur 404
     */
    @Test
    fun testNetworkProblem() = runTest {

        val errorResponseBody = "Error 404 message".toResponseBody("text/plain".toMediaType())

        // Le mock renverra une erreur 404
        coEvery {
            mockDataServiceAPI.getArticles()
        } returns Response.error(404, errorResponseBody)

        //when => Test réel de la fonction
        val listResult = run {
            cutArticleRepository.loadArticlesListSortByCategory().toList()
        }

        coVerify {
            mockDataServiceAPI.getArticles()
        }

        // On attend 2 valeurs dans le flow
        assertEquals(2, listResult.size)

        // Première valeur => Loading
        assertEquals(ResultCustom.Loading, listResult[0])

        // Deuxième valeur => Erreur
        assert(listResult[1] is ResultCustom.Failure)

    }

    /**
     * loadArticleByID
     */
    @Test
    fun test_loadArticleByID() = runTest {

        coEvery {
            mockDataServiceAPI.getArticles()
        } returns Response.success(mockDataServiceAPIResponse)


        coEvery {
            mockFakeAPIFeedback.getArticleFeedback(any())
        } returns mockFakeAPIFeedbackResponse


        //when => Test réel de la fonction
        val listResult = run {
            cutArticleRepository.loadArticleByID(0).toList()
        }

        coVerify {
            mockDataServiceAPI.getArticles()
            mockFakeAPIFeedback.getArticleFeedback(any())
        }

        // On attend 2 valeurs dans le flow
        assertEquals(2, listResult.size)

        // Première valeur => Loading
        assertEquals(ResultCustom.Loading, listResult[0])

        // Deuxième valeur => La réponse avec succès
        val res = listResult[1]

        assert(res is ResultCustom.Success)

        if (res is ResultCustom.Success){

            val article = res.value
            assertEquals(article.nIDArticle,0) // L'article 0 est renvoyé

        }
        else{
            assertFalse("Le résultat devrait être de type ResultCustom.Success",false)
        }

    }


}