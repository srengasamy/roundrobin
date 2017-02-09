package com.roundrobin.exceptions;

import org.springframework.http.HttpStatus;

import static com.roundrobin.common.ErrorTypes.INVALID_REQUEST_ERROR;

public class BadRequestException extends AbstractException {

    private static final long serialVersionUID = -6225887773316619787L;

    public BadRequestException(String errorCode) {
        super(INVALID_REQUEST_ERROR, errorCode, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String errorCode, String param) {
        super(INVALID_REQUEST_ERROR, errorCode, param, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String errorCode, Throwable t) {
        super(INVALID_REQUEST_ERROR, errorCode, HttpStatus.BAD_REQUEST, t);
    }

}
