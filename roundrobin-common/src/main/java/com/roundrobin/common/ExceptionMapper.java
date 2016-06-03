package com.roundrobin.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.exception.ClientException;

@ControllerAdvice
public class ExceptionMapper {

  @Autowired
  private ClientMessages messages;

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Response<String>> handleMessageNotReadableException(HttpMessageNotReadableException e) {
    Response<String> response = new Response<>(
        new Error(CommonErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(CommonErrorCode.UNPARSABLE_INPUT)));
    return log(HttpStatus.BAD_REQUEST, response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response<String>> handleBindException(MethodArgumentNotValidException ex) {
    List<Error> errors = ex.getBindingResult().getAllErrors().stream().map(e -> {
      String message = e.getDefaultMessage();
      if (e instanceof FieldError) {
        return new Error(CommonErrorCode.INVALID_FIELD, ((FieldError) e).getField() + ": " + message);
      } else {
        return new Error(CommonErrorCode.INVALID_FIELD, message);
      }
    }).collect(Collectors.toList());
    for (ObjectError oe : ex.getBindingResult().getAllErrors()) {
      System.out.print(oe);
    }
    return log(HttpStatus.BAD_REQUEST, new Response<>(errors));
  }

  @ExceptionHandler(ClientException.class)
  public ResponseEntity<Response<String>> handleClientException(ClientException ex) {
    Response<String> response = new Response<>(new Error(ex.getCode(), messages.getErrorMessage(ex.getCode())));
    return log(HttpStatus.BAD_REQUEST, response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Response<String>> handleException(Exception e) {
    Response<String> response = new Response<>(
        new Error(CommonErrorCode.INTERNAL_ERROR, messages.getErrorMessage(CommonErrorCode.INTERNAL_ERROR)));
    return log(HttpStatus.INTERNAL_SERVER_ERROR, response);
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class, NoHandlerFoundException.class})
  public ResponseEntity<Response<String>> handleMethodNotSupported(Exception e) {
    Response<String> response =
        new Response<>(new Error(CommonErrorCode.INVALID_URL, messages.getErrorMessage(CommonErrorCode.INVALID_URL)));
    return log(HttpStatus.NOT_FOUND, response);
  }

  private ResponseEntity<Response<String>> log(HttpStatus status, Response<String> response) {
    return new ResponseEntity<>(response, status);
  }
}
