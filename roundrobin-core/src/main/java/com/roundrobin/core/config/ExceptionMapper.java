package com.roundrobin.core.config;


import com.roundrobin.core.api.Error;
import com.roundrobin.core.api.Response;
import com.roundrobin.core.common.ClientMessages;
import com.roundrobin.core.exception.AbstractException;
import com.roundrobin.core.exception.BadRequestException;
import com.roundrobin.core.exception.NotFoundException;
import com.roundrobin.core.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.roundrobin.core.common.Constants.ERROR_CODE;
import static com.roundrobin.core.common.Constants.ERROR_MESSAGE;
import static com.roundrobin.core.common.Constants.ERROR_TYPE;
import static com.roundrobin.core.common.Constants.ERROR_UUID;
import static com.roundrobin.core.common.ErrorCodes.INVALID_FIELD;
import static com.roundrobin.core.common.ErrorCodes.INVALID_URL;
import static com.roundrobin.core.common.ErrorCodes.SERVICE_ERROR;
import static com.roundrobin.core.common.ErrorCodes.UNKNOWN_MEDIATYPE;
import static com.roundrobin.core.common.ErrorCodes.UNPARSABLE_INPUT;
import static net.logstash.logback.marker.Markers.append;

@ControllerAdvice
public class ExceptionMapper {
  private final Logger logger = LoggerFactory.getLogger(ExceptionMapper.class);

  @Autowired
  private ClientMessages messages;

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Response<Error>> handleMessageNotReadableException(HttpMessageNotReadableException e) {
    return log(new BadRequestException(UNPARSABLE_INPUT, e));
  }

  @ExceptionHandler(HttpMediaTypeException.class)
  public ResponseEntity<Response<Error>> handleMediaTypeException(HttpMediaTypeException e) {
    return log(new BadRequestException(UNKNOWN_MEDIATYPE, e));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Response<Error>> handleBindException(MethodArgumentNotValidException ex) {
    BadRequestException bex = new BadRequestException(INVALID_FIELD, ex);
    List<String> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(ex.getBindingResult().getAllErrors().stream().filter(e -> e instanceof FieldError)
            .map(e -> (FieldError) e).map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.toList()));
    fieldErrors.addAll(ex.getBindingResult().getAllErrors().stream().filter(e -> !(e instanceof FieldError))
            .map(e -> e.getDefaultMessage()).collect(Collectors.toList()));
    return log(bex, fieldErrors.isEmpty() ? null : fieldErrors);
  }

  @ExceptionHandler(AbstractException.class)
  public ResponseEntity<Response<Error>> handleAbstractException(AbstractException e) {
    return log(e);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Response<Error>> handleException(Throwable t) {
    return log(new ServiceException(SERVICE_ERROR, t));
  }

  @ExceptionHandler({HttpRequestMethodNotSupportedException.class, NoHandlerFoundException.class})
  public ResponseEntity<Response<Error>> handleMethodNotSupported(Exception e) {
    return log(new NotFoundException(INVALID_URL, e));
  }

  private ResponseEntity<Response<Error>> log(AbstractException exception) {
    return log(exception, null);
  }

  private ResponseEntity<Response<Error>> log(AbstractException exception, List<String> fieldErrors) {
    Error error = new Error();
    error.setType(exception.getErrorType());
    error.setCode(exception.getErrorCode());
    error.setParam(exception.getParam());
    error.setMessage(messages.getErrorMessage(exception.getErrorCode()));
    error.setFieldErrors(fieldErrors);
    Response<Error> response = new Response<Error>(error);
    response.setUuid(UUID.randomUUID().toString());
    logger.error(
            append(ERROR_TYPE, exception.getErrorType()).and(append(ERROR_CODE, exception.getErrorCode()))
                    .and(append(ERROR_MESSAGE, error.getMessage())).and(append(ERROR_UUID, response.getUuid())),
            "Exception:" + exception, exception);
    return new ResponseEntity<>(response, exception.getHttpStatus());
  }

}
