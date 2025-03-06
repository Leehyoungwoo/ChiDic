package com.chidicapp

import com.chidicapp.api.request.FeedPostCreateRequest
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.repository.UserRepository
import com.chidicdomain.type.Provider
import com.chidicdomain.type.Role
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
    fun `사용자 1000개 데이터베이스에 넣기`() {
        for (i in 1..1000) {
            val user = User(
                id = 0L,
                username = "$i",
                email = "$i@example.com",
                profilePicture = null,
                role = Role.USER,
                provider = Provider.KAKAO
            )

            // 유저 데이터 저장
            userRepository.save(user)
        }
    }

    @Test
    fun `사용자 1이 모든 다른 사용자 팔로우하기`() {
        val authHeader =
            "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImlkIjoxLCJyb2xlIjoiVVNFUiIsImV4cCI6MTc0MjgwMDM1Nn0.NEUajiDTsPpfADTdCMGhk5e_ZFWo7K_Sz2YOA55LgbI"

        for (userId in 2..1001) {
            // 팔로우 API URL
            val url = "http://localhost:8080/api/follow/$userId"

            // Authorization 헤더에 토큰 설정
            val headers = HttpHeaders()
            headers.set("Authorization", authHeader)

            // 헤더만 포함된 요청 엔티티 생성
            val entity = HttpEntity<String>(headers)

            // POST 요청 보내기
            val response: ResponseEntity<String> =
                testRestTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)
        }
    }

    @Test
    fun `모든 유저 100개의 글 작성`() {
        for (userId in 2..1001) {
            val tokenResponse: ResponseEntity<String> = testRestTemplate.exchange(
                "http://localhost:8080/api/make-access-token/$userId",
                HttpMethod.POST,
                HttpEntity.EMPTY,
                String::class.java
            )
            val token = tokenResponse.body
            if (token != null) {
                for (i in 1..100) {
                    val feedPostRequest = FeedPostCreateRequest(
                        title = "새글 $userId $i 번째 제목",
                        content = "내용 $userId $i 번째 내용",
                    )

                    val headers = HttpHeaders().apply {
                        set("Authorization", "Bearer $token")
                    }
                    val entity = HttpEntity(feedPostRequest, headers)

                    val postResponse: ResponseEntity<Void> = testRestTemplate.exchange(
                        "http://localhost:8080/api/feedposts", HttpMethod.POST, entity, Void::class.java
                    )

                    println("글 작성 요청 결과 : $postResponse.statusCode")
                }
            }
        }
    }

    @Test
    fun `모든 유저 1개의 글 작성`() {
        for (userId in 2..1001) {
            val tokenResponse: ResponseEntity<String> = testRestTemplate.exchange(
                "http://localhost:8080/api/make-access-token/$userId",
                HttpMethod.POST,
                HttpEntity.EMPTY,
                String::class.java
            )
            val token = tokenResponse.body
            if (token != null) {
                for (i in 1..1) {
                    val feedPostRequest = FeedPostCreateRequest(
                        title = "새글 $userId $i 번째 제목",
                        content = "내용 $userId $i 번째 내용",
                    )

                    val headers = HttpHeaders().apply {
                        set("Authorization", "Bearer $token")
                    }
                    val entity = HttpEntity(feedPostRequest, headers)

                    val postResponse: ResponseEntity<Void> = testRestTemplate.exchange(
                        "http://localhost:8080/api/feedposts", HttpMethod.POST, entity, Void::class.java
                    )

                    println("글 작성 요청 결과 : $postResponse.statusCode")
                }
            }
        }
    }


    @Test
    fun `50명의 유저가 1000개의 포스트에 좋아요`() {
        for (userId in 2..50) {
            val tokenResponse: ResponseEntity<String> = testRestTemplate.exchange(
                "http://localhost:8080/api/make-access-token/$userId",
                HttpMethod.POST,
                HttpEntity.EMPTY,
                String::class.java
            )
            val token = tokenResponse.body
            if (token != null) {
                for (postId in 1..1000) {
                    val headers = HttpHeaders().apply {
                        set("Authorization", "Bearer $token")
                        contentType = MediaType.APPLICATION_JSON
                    }
                    val entity = HttpEntity(null, headers)

                    val response: ResponseEntity<Void> = testRestTemplate.exchange(
                        "http://localhost:8080/api/feedposts/$postId/like",
                        HttpMethod.POST,
                        entity,
                        Void::class.java
                    )

                    if (response.statusCode.is2xxSuccessful) {
                        println("User $userId - Post $postId 좋아요 성공")
                    } else {
                        println("User $userId - Post $postId 실패: ${response.statusCode}")
                    }
                }
            }
        }
    }

    @Test
    fun `50명의 유저가 2000개의 포스트에 댓글 작성`() {
        for (userId in 2..51) {
            val tokenResponse: ResponseEntity<String> = testRestTemplate.exchange(
                "http://localhost:8080/api/make-access-token/$userId",
                HttpMethod.POST,
                HttpEntity.EMPTY,
                String::class.java
            )
            val token = tokenResponse.body
            if (token != null) {
                for (postId in 1..2000) {
                    val headers = HttpHeaders().apply {
                        set("Authorization", "Bearer $token")
                        contentType = MediaType.APPLICATION_JSON
                    }

                    val commentRequest = mapOf("content" to "댓글$userId")

                    val entity = HttpEntity(commentRequest, headers)

                    val response: ResponseEntity<Void> = testRestTemplate.exchange(
                        "http://localhost:8080/api/$postId/comments",
                        HttpMethod.POST,
                        entity,
                        Void::class.java
                    )

                    if (response.statusCode.is2xxSuccessful) {
                        println("User $userId - Post $postId 댓글 작성 성공")
                    } else {
                        println("User $userId - Post $postId 실패: ${response.statusCode}")
                    }
                }
            }
        }
    }

    @Test
    fun `50명의 유저가 1번 글에 댓글 100개 작성`() {
        for (userId in 1..1000) {
            val tokenResponse: ResponseEntity<String> = testRestTemplate.exchange(
                "http://localhost:8080/api/make-access-token/$userId",
                HttpMethod.POST,
                HttpEntity.EMPTY,
                String::class.java
            )

            val token = tokenResponse.body
            if (token != null) {
                // 1번 글에 100개의 댓글 달기
                for (commentNumber in 1..2) {
                    val headers = HttpHeaders().apply {
                        set("Authorization", "Bearer $token")
                        contentType = MediaType.APPLICATION_JSON
                    }

                    val commentRequest = mapOf("content" to "댓글$userId - $commentNumber 번째 댓글")

                    val entity = HttpEntity(commentRequest, headers)

                    val response: ResponseEntity<Void> = testRestTemplate.exchange(
                        "http://localhost:8080/api/1/comments",
                        HttpMethod.POST,
                        entity,
                        Void::class.java
                    )

                    if (response.statusCode.is2xxSuccessful) {
                        println("User $userId - Comment $commentNumber 작성 성공")
                    } else {
                        println("User $userId - Comment $commentNumber 실패: ${response.statusCode}")
                    }
                }
            } else {
                println("User $userId - 토큰 발급 실패")
            }
        }
    }

    @Test
    fun `1001명의 유저가 99981~100000번 포스트에 좋아요`() {
        for (userId in 1..1001) {
            val tokenResponse: ResponseEntity<String> = testRestTemplate.exchange(
                "http://localhost:8080/api/make-access-token/$userId",
                HttpMethod.POST,
                HttpEntity.EMPTY,
                String::class.java
            )
            val token = tokenResponse.body
            if (token != null) {
                for (postId in 99981..100000) {
                    val headers = HttpHeaders().apply {
                        set("Authorization", "Bearer $token")
                        contentType = MediaType.APPLICATION_JSON
                    }
                    val entity = HttpEntity(null, headers)

                    val response: ResponseEntity<Void> = testRestTemplate.exchange(
                        "http://localhost:8080/api/feedposts/$postId/like",
                        HttpMethod.POST,
                        entity,
                        Void::class.java
                    )

                    if (response.statusCode.is2xxSuccessful) {
                        println("User $userId - Post $postId 좋아요 성공")
                    } else {
                        println("User $userId - Post $postId 실패: ${response.statusCode}")
                    }
                }
            }
        }
    }

    // 핫키 테스트
    @Test
    fun `사용자 20000개 데이터베이스에 넣기`() {
        for (i in 1002..20000) {
            val user = User(
                id = 0L,
                username = "$i",
                email = "$i@example.com",
                profilePicture = null,
                role = Role.USER,
                provider = Provider.KAKAO
            )

            // 유저 데이터 저장
            userRepository.save(user)
        }
    }
    @Test
    fun `모든 유저가 특정 게시물에 좋아요`() {
        for (userId in 1..20000) {
            // 유저별로 액세스 토큰을 받기 위한 요청
            val tokenResponse: ResponseEntity<String> = testRestTemplate.exchange(
                "http://localhost:8080/api/make-access-token/$userId",
                HttpMethod.POST,
                HttpEntity.EMPTY,
                String::class.java
            )
            val token = tokenResponse.body
            if (token != null) {
                // 게시물 1번에 좋아요 요청을 위한 헤더 설정
                val headers = HttpHeaders().apply {
                    set("Authorization", "Bearer $token")
                    contentType = MediaType.APPLICATION_JSON
                }
                val entity = HttpEntity(null, headers)

                // 게시물 1번에 좋아요 요청 보내기
                val response: ResponseEntity<Void> = testRestTemplate.exchange(
                    "http://localhost:8080/api/feedposts/99860/like",
                    HttpMethod.POST,
                    entity,
                    Void::class.java
                )

                // 요청 결과 확인
                if (response.statusCode.is2xxSuccessful) {
                    println("User $userId - 게시물 1번 좋아요 성공")
                } else {
                    println("User $userId - 게시물 1번 좋아요 실패: ${response.statusCode}")
                }
            }
        }
    }
}