package com.roundrobin.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
    List<Error> errors = ex.getBindingResult().getAllErrors().stream().map(e -> {
      String message = e.getDefaultMessage();
      if (e instanceof FieldError) {
        return new Error(ErrorCode.INVALID_FIELD.getCode(), ((FieldError) e).getField() + ": " + message);
      } else {
        return new Error(ErrorCode.INVALID_FIELD.getCode(), message);
      }
    }).collect(Collectors.toList());
    for (ObjectError oe : ex.getBindingResult().getAllErrors()) {
      System.out.print(oe);
    }
    return log(HttpStatus.BAD_REQUEST, new Response<>(errors));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<Response<String>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    Response<String> response =
            new Response<>(new Error(ErrorCode.INVALID_URL.getCode(), messages.getErrorMessage(ErrorCode.INVALID_URL)));
    return log(HttpStatus.NOT_FOUND, response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Response<String>> handleMessageNotReadableException(HttpMessageNotReadableException e) {
    Response<String> response =
            new Response<>(new Error(ErrorCode.UNPARSABLE_INPUT.getCode(), messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT)));
    return log(HttpStatus.BAD_REQUEST, response);
  }

  private ResponseEntity<Response<String>> log(HttpStatus status, Response<String> response) {
    return new ResponseEntity<>(response, status);
  }

}
