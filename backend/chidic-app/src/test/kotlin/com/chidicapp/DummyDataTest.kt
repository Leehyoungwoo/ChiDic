package com.chidicapp

import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.repository.UserRepository
import com.chidicdomain.type.Provider
import com.chidicdomain.type.Role
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

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
        val authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdXRob3JpdGllcyI6IlJPTEVfVVNFUiIsImlkIjoxLCJyb2xlIjoiVVNFUiIsImV4cCI6MTc0MDMwODM1MX0.98liRzzMjI78FSPR9gjL4-HT17ncFswmkLjbXLAI9IM"

        for (userId in 2..1001) {
            // 팔로우 API URL
            val url = "http://localhost:8080/api/follow/$userId"

            // Authorization 헤더에 토큰 설정
            val headers = HttpHeaders()
            headers.set("Authorization", authHeader)

            // 헤더만 포함된 요청 엔티티 생성
            val entity = HttpEntity<String>(headers)

            // POST 요청 보내기
            val response: ResponseEntity<String> = testRestTemplate.exchange(url, HttpMethod.POST, entity, String::class.java)
        }
    }
}