package com.chidicapp.exception

import com.chidicapp.security.jwt.filter.RefreshTokenNotFoundException
import com.chidicdomain.domain.service.comment.CommentNotFoundException
import com.chidicdomain.domain.service.feedpost.FeedPostNotFoundException
import com.chidicdomain.domain.service.user.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionAdvice {
    @ExceptionHandler(UserNotFoundException::class, FeedPostNotFoundException::class, CommentNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(e: RuntimeException): String? {
        return e.message
    }

    @ExceptionHandler(RefreshTokenNotFoundException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadRequestException(e: RuntimeException): String? {
        return e.message
    }
}