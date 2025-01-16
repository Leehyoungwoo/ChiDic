package com.chidicapp.api.auth

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(("/api/login"))
class OAuth2Controller {

    @PostMapping("/kakao")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestHeader("X-Kakao-Token") kakaoToken: String): Unit{
        val token = null
        HttpHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer $token")
    }
}