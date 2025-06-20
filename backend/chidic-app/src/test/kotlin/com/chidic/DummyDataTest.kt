package com.chidic

import com.chidic.api.request.FeedPostCreateRequest
import com.chidic.domain.entity.User
import com.chidic.domain.repository.UserRepository
import com.chidic.type.Provider
import com.chidic.type.Role
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DummyDataTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun insert1000UsersIntoDatabase() {
        for (i in 1..1000) {
            val user = User(
                id = 0L,
                username = "$i",
                email = "$i@example.com",
                profilePicture = null,
                role = Role.USER,
                provider = Provider.KAKAO
            )
            userRepository.save(user)
        }
    }

    @Test
    fun user1FollowsAllOthers() {
        val authHeader = "Bearer ...your_token_here..."

        for (userId in 2..1001) {
            val url = "http://localhost:8080/api/follow/$userId"
            val headers = HttpHeaders().apply { set("Authorization", authHeader) }
            val entity = HttpEntity<String>(headers)
            testRestTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)
        }
    }

    @Test
    fun allUsersCreate100Posts() {
        for (userId in 2..1001) {
            val token = getToken(userId) ?: continue
            for (i in 1..100) {
                val request = FeedPostCreateRequest("Title $userId-$i", "Content $userId-$i")
                val headers = HttpHeaders().apply { set("Authorization", "Bearer $token") }
                val entity = HttpEntity(request, headers)
                testRestTemplate.exchange("http://localhost:8080/api/feedposts", HttpMethod.POST, entity, Void::class.java)
            }
        }
    }

    @Test
    fun allUsersCreate1Post() {
        for (userId in 2..1001) {
            val token = getToken(userId) ?: continue
            val request = FeedPostCreateRequest("Title $userId", "Content $userId")
            val headers = HttpHeaders().apply { set("Authorization", "Bearer $token") }
            val entity = HttpEntity(request, headers)
            testRestTemplate.exchange("http://localhost:8080/api/feedposts", HttpMethod.POST, entity, Void::class.java)
        }
    }

    @Test
    fun fiftyUsersLike1000Posts() {
        for (userId in 2..50) {
            val token = getToken(userId) ?: continue
            for (postId in 1..1000) {
                likePost(token, postId)
            }
        }
    }

    @Test
    fun fiftyUsersCommentOn2000Posts() {
        for (userId in 2..51) {
            val token = getToken(userId) ?: continue
            for (postId in 1..2000) {
                commentPost(token, postId, "Comment from user $userId")
            }
        }
    }

    @Test
    fun fiftyUsersComment100TimesOnPost1() {
        for (userId in 1..1000) {
            val token = getToken(userId) ?: continue
            for (i in 1..2) {
                commentPost(token, 1, "User $userId - Comment $i")
            }
        }
    }

    @Test
    fun thousandUsersLikeLast20Posts() {
        for (userId in 1..1001) {
            val token = getToken(userId) ?: continue
            for (postId in 99981..100000) {
                likePost(token, postId)
            }
        }
    }

    @Test
    fun insertUsersFrom1002To20000() {
        for (i in 1002..20000) {
            val user = User(0L, "$i", "$i@example.com", null, Role.USER, Provider.KAKAO)
            userRepository.save(user)
        }
    }

    @Test
    fun allUsersLikeSpecificPost() {
        for (userId in 1..20000) {
            val token = getToken(userId) ?: continue
            likePost(token, 1555)
        }
    }

    private fun getToken(userId: Int): String? {
        val response = testRestTemplate.exchange(
            "http://localhost:8080/api/make-access-token/$userId",
            HttpMethod.POST,
            HttpEntity.EMPTY,
            String::class.java
        )
        return response.body
    }

    private fun likePost(token: String, postId: Int) {
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(null, headers)
        testRestTemplate.exchange(
            "http://localhost:8080/api/feedposts/$postId/like",
            HttpMethod.POST,
            entity,
            Void::class.java
        )
    }

    private fun commentPost(token: String, postId: Int, content: String) {
        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $token")
            contentType = MediaType.APPLICATION_JSON
        }
        val entity = HttpEntity(mapOf("content" to content), headers)
        testRestTemplate.exchange(
            "http://localhost:8080/api/$postId/comments",
            HttpMethod.POST,
            entity,
            Void::class.java
        )
    }
}
