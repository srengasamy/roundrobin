package com.roundrobin.common;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.codec.Base64;

import com.roundrobin.api.Response;
import com.roundrobin.api.Token;
import com.roundrobin.helper.TestHttpHelper;

@Ignore
public abstract class AbstractResourceTests {
  @Autowired
  protected TestHttpHelper helper;

  @Autowired
  protected ClientMessages messages;

  protected final String authUrl = "http://localhost:8080/roundrobin-auth/";
  protected final String vaultUrl = "http://localhost:9090/roundrobin-vault/";

  protected void createUser(String username) {
    createUser(username, false);
  }

  protected String createUser(boolean vendor) {
    String username = "testing" + System.currentTimeMillis() + "@testing.com";
    return createUser(username, vendor);
  }

  protected String createUser() {
    String username = "testing" + System.currentTimeMillis() + "@testing.com";
    return createUser(username, false);
  }

  protected String createUser(String username, boolean vendor) {
    String userTo = "{\"username\":\"" + username + "\", \"password\":\"testing\", \"vendor\":" + vendor + "}";
    Response<Boolean> created =
        helper.post(authUrl + "user-action/create-user", userTo, new ParameterizedTypeReference<Response<Boolean>>() {})
            .getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity(), is(true));
    return username;
  }

  protected Token getToken(String username) {
    return getToken(username, "testing");
  }

  protected Token getToken(String username, String password) {
    Token token = helper
        .post(authUrl + "oauth/token?grant_type=password&username={username}&password={password}",
            createWebClientHeaders(), (String) null, new ParameterizedTypeReference<Token>() {}, username, password)
        .getBody();
    assertThat(token, notNullValue());
    assertThat(token.getAccessToken(), notNullValue());
    return token;
  }

  protected String getAccessToken(String username) {
    return getToken(username).getAccessToken();
  }

  protected HttpHeaders createWebClientHeaders() {
    return new HttpHeaders() {
      private static final long serialVersionUID = 2987123839863076324L;
      {
        String auth = "web2:secret";
        byte[] encodedAuth = Base64.encode(auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        set("Authorization", authHeader);
      }
    };
  }

  protected HttpHeaders createBearerHeaders(String token) {
    return new HttpHeaders() {
      private static final long serialVersionUID = 2987123839863076324L;
      {
        String authHeader = "Bearer " + token;
        set("Authorization", authHeader);
      }
    };
  }

  protected HttpHeaders createBearerHeaders() {
    return createBearerHeaders(getToken(createUser()).getAccessToken());
  }
}
