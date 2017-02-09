package com.roundrobin.exceptions;

import org.springframework.http.HttpStatus;

import static com.roundrobin.common.ErrorTypes.INVALID_REQUEST_ERROR;

public class NotFoundException extends AbstractException {
    private static final long serialVersionUID = 8912137173577075314L;

    public NotFoundException(String errorCode) {
        super(INVALID_REQUEST_ERROR, errorCode, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String errorCode, Throwable t) {
        super(INVALID_REQUEST_ERROR, errorCode, HttpStatus.NOT_FOUND, t);

    }
}
