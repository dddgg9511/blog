package com.choo.blog.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String token) {
        super("유효하지 않은 토큰입니다. token: " + token);
    }
}
