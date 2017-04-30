package com.roundrobin.test.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.roundrobin.core.api.Response;
import com.roundrobin.core.config.ClientMessages;
import com.roundrobin.test.api.Token;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@Ignore
public abstract class AbstractResourceTests {
  @Autowired
  protected TestRestTemplate template;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  protected ClientMessages clientMessages;

  @Value("${keystore.password}")
  private String keystorePassword;

  protected final String authUrl = "http://localhost:8080/auth/";
  protected final String vaultUrl = "http://localhost:9090/vault/";
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

  protected String getToken() {
    return getToken(createUser()).getAccessToken();
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

  protected HttpEntity<?> createHttpEntity(MediaType mediaType, Object entity, HttpHeaders headers) {
    headers.setContentType(mediaType);
    return new HttpEntity<>(entity, headers);
  }

  protected HttpEntity<?> createHttpEntity(Object entity, HttpHeaders headers) {
    return createHttpEntity(MediaType.APPLICATION_JSON, entity, headers);
  }

  protected String getAccessToken(String username) {
    return getToken(username).getAccessToken();
  }


  protected String getMockAccessToken(String userId, long expiry, List<String> scopes, List<String> resources) {
    Map<String, Object> token = new HashMap<>();
    token.put("user_name", userId);
    token.put("exp", expiry);
    token.put("scope", scopes);
    token.put("aud", resources);
    return createMockAccessToken(token);
  }

  protected String getMockAccessToken() {
    return getMockAccessToken(UUID.randomUUID().toString());
  }

  protected String getMockAccessToken(String userId) {
    return getMockAccessToken(userId,
            (System.currentTimeMillis() / 1000) + 1800,
            getValidScopes(),
            getValidResources());
  }

  private String createMockAccessToken(Map<String, Object> payload) {
    try {
      return createMockAccessToken(objectMapper.writeValueAsString(payload));
    } catch (Exception e) {
      return "error";
    }
  }

  private String createMockAccessToken(String payload) {
    KeyStoreKeyFactory keyStoreKeyFactory =
            new KeyStoreKeyFactory(new ClassPathResource("roundrobin-auth.jks"), keystorePassword.toCharArray());
    KeyPair pair = keyStoreKeyFactory.getKeyPair("roundrobin-auth");
    return Jwts.builder()
            .setPayload(payload)
            .signWith(SignatureAlgorithm.RS256, pair.getPrivate())
            .compact();
  }

  protected List<String> getValidResources() {
    List<String> resources = new ArrayList<>();
    resources.add("vault");
    resources.add("auth");
    resources.add("api");
    return resources;
  }

  protected List<String> getValidScopes() {
    List<String> scopes = new ArrayList<>();
    scopes.add("profile");
    scopes.add("location");
    scopes.add("vault");
    scopes.add("api");
    scopes.add("skill");
    return scopes;
  }

  protected List<String> getInvalidScopes() {
    List<String> scopes = new ArrayList<>();
    scopes.add("abc");
    return scopes;
  }

  protected List<String> getInvalidResources() {
    List<String> scopes = new ArrayList<>();
    scopes.add("abc");
    return scopes;
  }
}
