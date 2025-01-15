package com.domain.repository

import com.chidicdomain.ChidicDomainApplication
import com.chidicdomain.domain.entity.User
import com.chidicdomain.domain.repository.UserRepository
import jakarta.persistence.EntityManager
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@ContextConfiguration(classes = [ChidicDomainApplication::class])
@ActiveProfiles(profiles = ["test"])
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var entityManager: EntityManager ;

    @Test
    fun `save and find user by username`() {
        // Given: 테스트 데이터 생성
        val user = User(
            username = "testuser",
            email = "testuser@example.com"
        )
        userRepository.save(user)
        entityManager.flush();
        entityManager.clear();

        // When: `findByUsername` 메서드 호출
        val foundUser = userRepository.findByUsername("testuser")

        // Then: 결과 검증
        assertEquals(1, foundUser?.id)
        assertEquals("testuser", foundUser?.username)
        assertEquals("testuser@example.com", foundUser?.email)
    }

    @Test
    fun `return null if username not found`() {
        // When: 존재하지 않는 사용자 이름으로 검색
        val foundUser = userRepository.findByUsername("nonexistentuser")

        // Then: null 반환 확인
        assertNull(foundUser)
    }

    @Test
    fun `deleted user should not be found`() {
        // given
        val user = User(
            username = "testuser",
            email = "testuser@example.com"
        )
        val saveUser = userRepository.save(user)
        entityManager.flush();

        saveUser.deleteData()

        val foundUser = userRepository.findByUsername("testuser")
        assertNull(foundUser)
    }
}
