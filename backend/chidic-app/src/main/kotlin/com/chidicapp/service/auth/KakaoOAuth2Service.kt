package com.chidicapp.service.auth

import com.chidicdomain.dto.OAuth2UserInfoDto
import com.chidicdomain.enum.Provider
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class KakaoOAuth2Service(
    private val restTemplate: RestTemplate,
    private val objectMapper: ObjectMapper,
    @Value("\${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private val kakaoUserInfoURL: String
): OAuthService {

    override fun getUserInfo(accessToken: String): OAuth2UserInfoDto {
        val responseBody = fetchKakaoUserInfo(accessToken)
        return parseKakaoUserInfo(responseBody)
    }

    private fun fetchKakaoUserInfo(accessToken: String): Map<*, *> =
        objectMapper.readValue(
            restTemplate.exchange(
                kakaoUserInfoURL,
                HttpMethod.GET,
                HttpEntity<Any>(HttpHeaders().apply { set("Authorization", "Bearer $accessToken") }),
                String::class.java
            ).body, Map::class.java
        )

    private fun parseKakaoUserInfo(responseBody: Map<*, *>): OAuth2UserInfoDto {
        val kakaoAccount = responseBody["kakao_account"] as? Map<*, *> ?: emptyMap<Any, Any>()

        return OAuth2UserInfoDto(
            username = responseBody["id"]?.toString() ?: throw IllegalStateException("Missing id in response"),
            email = kakaoAccount["email"]?.toString().orEmpty(),
            provider = Provider.KAKAO
        )
    }
}