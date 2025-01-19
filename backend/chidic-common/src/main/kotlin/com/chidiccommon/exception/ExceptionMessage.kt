package com.chidiccommon.exception

enum class ExceptionMessage(val message: String) {
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_FOUND("토큰이 존재하지 않습니다.")
}