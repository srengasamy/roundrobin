package com.roundrobin.common;

import static net.logstash.logback.marker.Markers.append;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;

@ControllerAdvice
public class ExceptionMapper {
  private final Logger logger = LoggerFactory.getLogger(ExceptionMapper.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response<String>> handleBindException(MethodArgumentNotValidException ex) {
    Response<String> response = new Response<>();
    response.setUuid(UUID.randomUUID().toString());
    logger.error(
        append(Constants.ERROR_UUID, response.getUuid()).and(append(Constants.ERROR_CODE, ErrorCode.INVALID_FIELD))
            .and(append(Constants.ERROR_MESSAGE_NAME, "Multiple field errors")),
        "Validation errors:" + ex, ex);
    List<Error> errors = new ArrayList<>();
    errors.addAll(ex.getBindingResult().getAllErrors().stream().filter(e -> e instanceof ObjectError)
        .map(e -> (ObjectError) e).map(e -> {
          String message = e.getDefaultMessage();
          return new Error(ErrorCode.INVALID_FIELD,
              (e instanceof FieldError ? ((FieldError) e).getField() : e.getObjectName()) + ": " + message);
        }).collect(Collectors.toList()));
    response.setErrors(errors);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

}
