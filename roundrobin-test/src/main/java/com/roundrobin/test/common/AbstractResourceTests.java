package com.roundrobin.test.common;

import com.roundrobin.core.api.Response;
import com.roundrobin.core.config.ClientMessages;
import com.roundrobin.test.api.Token;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@Ignore
public abstract class AbstractResourceTests {
  @Autowired
  protected TestRestTemplate template;

  @Autowired
  protected ClientMessages clientMessages;

  protected final String authUrl = "http://localhost:8080/roundrobin-auth/";
  protected final String vaultUrl = "http://localhost:9090/roundrobin-vault/";
  protected final String tokenUrl = authUrl + "oauth/token?grant_type=password&username={username}&password={password}";

  protected String createUsername() {
    return "testing" + System.currentTimeMillis() + "@testing.com";
  }

  protected String createUser() {
    return createUser(createUsername());
  }

  protected String createUser(String username) {
    Map<String, String> userTo = new HashMap<>();
    userTo.put("username", username);
    userTo.put("password", "testing");
    Response<Boolean> created = template.exchange(authUrl + "user-action/create-user",
            HttpMethod.POST,
            createHttpEntity(userTo),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity(), is(true));
    return username;
  }

  protected Token getToken(String username) {
    return getToken(username, "testing");
  }

  protected Token getToken(String username, String password) {
    Token token = template
            .exchange(authUrl + "oauth/token?grant_type=password&username={username}&password={password}",
                    HttpMethod.POST,
                    createHttpEntity(createAuthHeader()),
                    new ParameterizedTypeReference<Token>() {
                    }, username, password).getBody();
    assertThat(token, notNullValue());
    assertThat(token.getAccessToken(), notNullValue());
    return token;
  }

  protected String getAccessToken(String username) {
    return getToken(username).getAccessToken();
  }

  protected HttpHeaders createBearerHeader() {
    return createBearerHeader(getToken(createUser()).getAccessToken());
  }

  protected HttpHeaders createBearerHeader(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + token);
    return headers;
  }

  protected HttpHeaders createAuthHeader() {
    return createAuthHeader("web", "secret");
  }

  protected HttpHeaders createAuthHeader(String username, String password) {
    HttpHeaders headers = new HttpHeaders();
    String auth = username + ":" + password;
    headers.add("Authorization", "Basic " + new String(Base64.getEncoder().encode(auth.getBytes())));
    return headers;
  }

  protected HttpEntity<?> createHttpEntity() {
    return createHttpEntity(null, new HttpHeaders());
  }

  protected HttpEntity<?> createHttpEntity(HttpHeaders headers) {
    return createHttpEntity(null, headers);
  }

  protected HttpEntity<?> createHttpEntity(Object entity) {
    return createHttpEntity(entity, new HttpHeaders());
  }

  protected HttpEntity<?> createHttpEntity(Object entity, HttpHeaders headers) {
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<>(entity, headers);
  }

}
