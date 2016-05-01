package com.roundrobin.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.exception.ClientException;

@ControllerAdvice
public class ExceptionMapper {

  @Autowired
  private ClientMessages messages;

  @ExceptionHandler(ClientException.class)
  public ResponseEntity<Response<String>> handleClientException(ClientException ex) {
    Response<String> response =
        new Response<>(new Error(ex.getCode().getCode(), messages.getErrorMessage(ex.getCode())));
    return log(HttpStatus.BAD_REQUEST, response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response<String>> handleBindException(MethodArgumentNotValidException ex) {
    List<Error> errors = ex.getBindingResult().getAllErrors().stream().filter(e -> e instanceof FieldError)
        .map(e -> (FieldError) e).map(e -> {
          String message = e.getDefaultMessage();
          return new Error(ErrorCode.INVALID_FIELD.getCode(), e.getField() + ": " + message);
        }).collect(Collectors.toList());
    return log(HttpStatus.BAD_REQUEST, new Response<>(errors));
  }

  private ResponseEntity<Response<String>> log(HttpStatus status, Response<String> response) {
    return new ResponseEntity<>(response, status);
  }

}
