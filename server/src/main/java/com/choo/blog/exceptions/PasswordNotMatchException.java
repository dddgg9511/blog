package com.choo.blog.exceptions;

public class PasswordNotMatchException extends RuntimeException{
    public PasswordNotMatchException(String email){
        super("비밀번호가 일치하지 않습니다. email : " + email);
    }
}
