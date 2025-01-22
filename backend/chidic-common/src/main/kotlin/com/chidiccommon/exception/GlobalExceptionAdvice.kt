package com.chidiccommon.exception

import com.chidiccommon.exception.exceptions.CommentNotFoundException
import com.chidiccommon.exception.exceptions.FeedPostNotFoundException
import com.chidiccommon.exception.exceptions.RefreshTokenNotFoundException
import com.chidiccommon.exception.exceptions.UserNotFoundException
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