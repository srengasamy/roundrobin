package com.roundrobin.exceptions;

import org.springframework.http.HttpStatus;

import static com.roundrobin.common.ErrorTypes.AUTHENTICATION_ERROR;

public class AuthException extends AbstractException {
    private static final long serialVersionUID = -6039187233464180033L;

    public AuthException(String errorCode) {
        super(AUTHENTICATION_ERROR, errorCode, HttpStatus.UNAUTHORIZED);
    }

    public AuthException(String errorCode, Throwable t) {
        super(AUTHENTICATION_ERROR, errorCode, HttpStatus.UNAUTHORIZED, t);
    }
}
