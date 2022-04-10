package com.choo.blog.exceptions;

public class LoginFailException extends RuntimeException{
    public LoginFailException(String email){
        super("로그인에 실패하였습니다. : " + email);
    }
}
