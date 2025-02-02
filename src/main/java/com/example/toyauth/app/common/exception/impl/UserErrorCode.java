package com.example.toyauth.app.common.exception.impl;


import com.example.toyauth.app.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "user not exist"),

    ;

    private final HttpStatus httpStatus;
    private final String message;


}
