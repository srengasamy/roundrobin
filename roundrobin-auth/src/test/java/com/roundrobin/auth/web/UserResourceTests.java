package com.roundrobin.auth.web;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.core.api.Response;
import com.roundrobin.test.api.Token;
import com.roundrobin.test.api.UnauthorizedError;
import com.roundrobin.auth.api.UserTo;

public class UserResourceTests extends ResourceTests {
  @Test
  public void testUpdate() {
    String username = createUser();
    Token token = getToken(username);
    UserTo userTo = new UserTo();
    userTo.setVendor(Optional.of(true));
    Response<Boolean> updated = helper.put(authUrl + "admin/user", createBearerHeaders(token.getAccessToken()), userTo,
        new ParameterizedTypeReference<Response<Boolean>>() {}).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getEntity(), is(true));
  }

  @Test
  public void testUpdateRemoveVendor() {
    String username = createUser(true);
    Token token = getToken(username);
    UserTo userTo = new UserTo();
    userTo.setVendor(Optional.of(false));
    Response<Boolean> updated = helper.put(authUrl + "admin/user", createBearerHeaders(token.getAccessToken()), userTo,
        new ParameterizedTypeReference<Response<Boolean>>() {}).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getEntity(), is(true));
  }

  @Test
  public void testUpdateWithEmptyToken() {
    UserTo userTo = new UserTo();
    userTo.setVendor(Optional.of(true));
    UnauthorizedError updated =
        helper.put(authUrl + "admin/user", userTo, new ParameterizedTypeReference<UnauthorizedError>() {}).getBody();
    assertThat(updated.getError(), is("unauthorized"));
  }

  @Test
  public void testUpdateWithInvalidToken() {
    UserTo userTo = new UserTo();
    userTo.setVendor(Optional.of(true));
    UnauthorizedError updated = helper.put(authUrl + "admin/user", createBearerHeaders("testing"), userTo,
        new ParameterizedTypeReference<UnauthorizedError>() {}).getBody();
    assertThat(updated.getError(), is("invalid_token"));
  }

  @Test
  public void testDelete() {
    String username = createUser();
    Token token = getToken(username);
    Response<Boolean> read = helper.delete(authUrl + "admin/user", createBearerHeaders(token.getAccessToken()),
        new ParameterizedTypeReference<Response<Boolean>>() {}).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity(), is(true));
    token = helper
        .post(authUrl + "oauth/token?grant_type=password&username={username}&password={password}",
            createWebClientHeaders(), (String) null, new ParameterizedTypeReference<Token>() {}, username, "testing")
        .getBody();
    assertThat(token.getAccessToken(), nullValue());
  }

  @Test
  public void testDeleteWithEmptyToken() {
    UnauthorizedError read =
        helper.delete(authUrl + "admin/user", new ParameterizedTypeReference<UnauthorizedError>() {}).getBody();
    assertThat(read.getError(), is("unauthorized"));
  }

  @Test
  public void testDeleteWithInvalidToken() {
    UnauthorizedError read = helper.delete(authUrl + "admin/user", createBearerHeaders("testing"),
        new ParameterizedTypeReference<UnauthorizedError>() {}).getBody();
    assertThat(read.getError(), is("invalid_token"));
  }
}
