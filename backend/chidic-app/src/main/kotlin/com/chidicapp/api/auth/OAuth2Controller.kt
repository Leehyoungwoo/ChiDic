package com.chidicapp.api.auth

import com.chidicapp.service.auth.AuthService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(("/api/login"))
class OAuth2Controller(
    private val authService: AuthService
) {

    @PostMapping("/kakao")
    @ResponseStatus(HttpStatus.OK)
    fun login(@RequestHeader("X-Kakao-Token") kakaoToken: String): Unit{
        authService.loginOrRegister(kakaoToken)
    }
}