package com.chidicapp

import com.chidicdomain.domain.repository.FeedPostRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeedPostLikeConcurrencyTest {
    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    lateinit var feedPostRepository: FeedPostRepository

    @Test
    fun `10명의 유저가 동시에 글 2024를 좋아요 요청`() {
        val executorService: ExecutorService = Executors.newFixedThreadPool(10) // 10개 스레드
        val latch = CountDownLatch(1) // 모든 스레드가 동시에 시작하도록 동기화

        for (userId in 1..10) {
            executorService.execute {
                try {
                    // 1. 토큰 발급 요청
                    val tokenResponse: ResponseEntity<String> = testRestTemplate.exchange(
                        "http://localhost:8080/api/make-access-token/$userId",
                        HttpMethod.POST,
                        HttpEntity.EMPTY,
                        String::class.java
                    )
                    val token = tokenResponse.body
                    if (token.isNullOrBlank()) {
                        println("User $userId: 토큰 발급 실패, 요청 중단")
                        return@execute
                    }

                    // 2. Authorization 헤더 설정
                    val headers = HttpHeaders().apply {
                        set("Authorization", "Bearer $token")
                    }
                    val requestEntity = HttpEntity<Void>(headers)

                    latch.await() // 모든 스레드가 준비될 때까지 대기

                    // 3. 좋아요 요청
                    val likeResponse: ResponseEntity<Void> = testRestTemplate.exchange(
                        "http://localhost:8080/api/feedposts/3001/like",
                        HttpMethod.POST,
                        requestEntity,
                        Void::class.java
                    )

                    println("$userId 의 좋아요 요청 결과 : ${likeResponse.statusCode}")
                } catch (e: Exception) {
                    println("Error in user $userId: ${e.message}")
                }
            }
        }

        Thread.sleep(1000) // 모든 스레드가 준비될 시간을 조금 줌
        latch.countDown() // 모든 스레드 동시 시작

        executorService.shutdown()
        executorService.awaitTermination(10, TimeUnit.SECONDS) // 모든 요청이 끝날 때까지 대기

        // 💡 DB 값이 업데이트될 시간을 기다리기 (없으면 0일 수도 있음)
        Thread.sleep(1000)

        val feedPost = feedPostRepository.findById(3001L)
        Assertions.assertTrue(feedPost.isPresent, "게시물이 존재해야 합니다.")
        Assertions.assertEquals(10, feedPost.get().likeCount)
    }
}