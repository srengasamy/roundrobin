package com.roundrobin.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;

@ControllerAdvice
public class ExceptionMapper {

  @Autowired
  private ClientMessages messages;


  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Response<String>> handleMessageNotReadableException(HttpMessageNotReadableException e) {
    Response<String> response = new Response<>(
        new Error(ErrorCode.UNPARSABLE_INPUT.getCode(), messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT)));
    return log(HttpStatus.BAD_REQUEST, response);
  }

  private ResponseEntity<Response<String>> log(HttpStatus status, Response<String> response) {
    return new ResponseEntity<>(response, status);
  }
}
