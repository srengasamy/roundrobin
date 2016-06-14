package com.roundrobin.helper;

import java.util.Optional;

import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Scope("singleton")
public class HttpHelper {
  protected RestTemplate template;
  protected MediaType mediaType;
 
  public HttpHelper() {
    template = new RestTemplate();
    mediaType = MediaType.APPLICATION_JSON;
  }

  public HttpHelper(RestTemplate template) {
    this.template = template;
    mediaType = MediaType.APPLICATION_JSON;
  }

  public HttpHelper(MediaType mediaType) {
    this.mediaType = mediaType;
  }

  protected <T, E> ResponseEntity<T> call(String url, HttpMethod httpMethod, Optional<E> entity,
      ParameterizedTypeReference<T> responseType, Object... uriVariables) {
    return call(url, httpMethod, Optional.empty(), entity, responseType, uriVariables);
  }

  protected <T, E> ResponseEntity<T> call(String url, HttpMethod httpMethod, Optional<HttpHeaders> httpHeaders,
      Optional<E> entity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
    HttpHeaders headers = httpHeaders.orElse(new HttpHeaders());
    headers.setContentType(mediaType);
    HttpEntity<E> requestEntity = new HttpEntity<E>(entity.orElse(null), headers);
    return template.exchange(url, httpMethod, requestEntity, responseType, uriVariables);
  }

  public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
    return call(url, HttpMethod.GET, Optional.empty(), responseType, uriVariables);
  }

  public <T> ResponseEntity<T> get(String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType,
      Object... uriVariables) {
    return call(url, HttpMethod.GET, Optional.of(headers), Optional.empty(), responseType, uriVariables);
  }

  public <T, E> ResponseEntity<T> put(String url, E entity, ParameterizedTypeReference<T> responseType,
      Object... uriVariables) {
    return call(url, HttpMethod.PUT, Optional.ofNullable(entity), responseType, uriVariables);
  }

  public <T, E> ResponseEntity<T> put(String url, HttpHeaders headers, E entity,
      ParameterizedTypeReference<T> responseType, Object... uriVariables) {
    return call(url, HttpMethod.PUT, Optional.of(headers), Optional.ofNullable(entity), responseType, uriVariables);
  }

  public <T, E> ResponseEntity<T> post(String url, E entity, ParameterizedTypeReference<T> responseType,
      Object... uriVariables) {
    return call(url, HttpMethod.POST, Optional.ofNullable(entity), responseType, uriVariables);
  }

  public <T, E> ResponseEntity<T> post(String url, HttpHeaders headers, E entity,
      ParameterizedTypeReference<T> responseType, Object... uriVariables) {
    return call(url, HttpMethod.POST, Optional.of(headers), Optional.ofNullable(entity), responseType, uriVariables);
  }

  public <T> ResponseEntity<T> delete(String url, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
    return call(url, HttpMethod.DELETE, Optional.empty(), responseType, uriVariables);
  }

  public <T> ResponseEntity<T> delete(String url, HttpHeaders headers, ParameterizedTypeReference<T> responseType,
      Object... uriVariables) {
    return call(url, HttpMethod.DELETE, Optional.of(headers), Optional.empty(), responseType, uriVariables);
  }

  public <T> ResponseEntity<T> get(String url, Class<T> responseType, Object... uriVariables)
      throws RestClientException {
    return get(url, new ParameterizedTypeReference<T>() {}, uriVariables);
  }

  public <T, E> ResponseEntity<T> put(String url, E entity, Class<T> responseType, Object... uriVariables) {
    return put(url, entity, new ParameterizedTypeReference<T>() {}, uriVariables);
  }

  public <T, E> ResponseEntity<T> post(String url, E entity, Class<T> responseType, Object... uriVariables) {
    return post(url, entity, new ParameterizedTypeReference<T>() {}, uriVariables);
  }

  public <T> ResponseEntity<T> delete(String url, Class<T> responseType, Object... uriVariables) {
    return delete(url, new ParameterizedTypeReference<T>() {}, uriVariables);
  }

  public RestTemplate getTemplate() {
    return template;
  }
}
