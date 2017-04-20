package com.roundrobin.auth.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.core.api.User;
import com.roundrobin.test.api.Token;
import com.roundrobin.test.api.UnauthorizedError;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class UserResourceTests extends ResourceTests {

  @Test
  public void testDelete() {
    String username = createUsername();
    Token token = getToken(createUser(username));
    Response<Boolean> delete = template.exchange(authUrl + "admin/user",
            HttpMethod.DELETE,
            createHttpEntity(createBearerHeader(token.getAccessToken())),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }).getBody();

    assertThat(delete.getEntity(), notNullValue());
    assertThat(delete.getEntity(), is(true));
    token = template.exchange(tokenUrl, HttpMethod.POST, createHttpEntity(createAuthHeader()), Token.class,
            username, "testing").getBody();
    assertThat(token.getAccessToken(), nullValue());
  }

  @Test
  public void testDeleteWithEmptyToken() {
    UnauthorizedError read = template.exchange(authUrl + "admin/user", HttpMethod.DELETE,
            createHttpEntity(), UnauthorizedError.class).getBody();
    assertThat(read.getError(), is("unauthorized"));
  }

  @Test
  public void testDeleteWithInvalidToken() {
    UnauthorizedError read = template.exchange(authUrl + "admin/user", HttpMethod.DELETE,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(read.getError(), is("invalid_token"));
  }

  @Test
  public void testRead() {
    String username = createUsername();
    Token token = getToken(createUser(username));
    Response<User> read = template.exchange(authUrl + "admin/user",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader(token.getAccessToken())),
            new ParameterizedTypeReference<Response<User>>() {
            }).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getUserId(), notNullValue());
    assertThat(read.getEntity().getUsername(), notNullValue());
    assertThat(read.getEntity().getRoles().size(), not(0));
    assertThat(read.getEntity().isVerified(), notNullValue());
  }

  @Test
  public void testReadWithEmptyToken() {
    UnauthorizedError read = template.exchange(authUrl + "admin/user", HttpMethod.GET,
            createHttpEntity(), UnauthorizedError.class).getBody();
    assertThat(read.getError(), is("unauthorized"));
  }

  @Test
  public void testReadWithInvalidToken() {
    UnauthorizedError read = template.exchange(authUrl + "admin/user", HttpMethod.GET,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(read.getError(), is("invalid_token"));
  }

  @Test
  public void test404() {
    String username = createUsername();
    Token token = getToken(createUser(username));
    UnauthorizedError read = template.exchange(authUrl + "abcd", HttpMethod.GET,
            createHttpEntity(createBearerHeader(token.getAccessToken())), UnauthorizedError.class).getBody();
    assertThat(read.getError(), is("Not Found"));
    assertThat(read.getMessage(), is("No message available"));
  }
}
