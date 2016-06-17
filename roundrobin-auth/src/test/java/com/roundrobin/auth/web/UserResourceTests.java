package com.roundrobin.auth.web;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.api.Response;
import com.roundrobin.api.Token;
import com.roundrobin.api.UnauthorizedError;
import com.roundrobin.auth.api.UserTo;

public class UserResourceTests extends ResourceTests {
  @Test
  public void testUpdate() {
    String username = createUser();
    Token token = getToken(username);
    UserTo userTo = new UserTo();
    userTo.setVendor(Optional.of(true));
    Response<Boolean> updated = helper.put(url + "admin/user", createBearerHeaders(token.getAccessToken()), userTo,
        new ParameterizedTypeReference<Response<Boolean>>() {}, port).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getEntity(), is(true));
  }

  @Test
  public void testUpdateRemoveVendor() {
    String username = createUser(true);
    Token token = getToken(username);
    UserTo userTo = new UserTo();
    userTo.setVendor(Optional.of(false));
    Response<Boolean> updated = helper.put(url + "admin/user", createBearerHeaders(token.getAccessToken()), userTo,
        new ParameterizedTypeReference<Response<Boolean>>() {}, port).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getEntity(), is(true));
  }

  @Test
  public void testUpdateWithEmptyToken() {
    UserTo userTo = new UserTo();
    userTo.setVendor(Optional.of(true));
    UnauthorizedError updated =
        helper.put(url + "admin/user", userTo, new ParameterizedTypeReference<UnauthorizedError>() {}, port).getBody();
    assertThat(updated.getError(), is("unauthorized"));
  }

  @Test
  public void testUpdateWithInvalidToken() {
    UserTo userTo = new UserTo();
    userTo.setVendor(Optional.of(true));
    UnauthorizedError updated = helper.put(url + "admin/user", createBearerHeaders("testing"), userTo,
        new ParameterizedTypeReference<UnauthorizedError>() {}, port).getBody();
    assertThat(updated.getError(), is("invalid_token"));
  }

  @Test
  public void testDelete() {
    String username = createUser();
    Token token = getToken(username);
    Response<Boolean> read = helper.delete(url + "admin/user", createBearerHeaders(token.getAccessToken()),
        new ParameterizedTypeReference<Response<Boolean>>() {}, port).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity(), is(true));
    token = getToken(username);
    assertThat(token.getAccessToken(), nullValue());
  }

  @Test
  public void testDeleteWithEmptyToken() {
    UnauthorizedError read =
        helper.delete(url + "admin/user", new ParameterizedTypeReference<UnauthorizedError>() {}, port).getBody();
    assertThat(read.getError(), is("unauthorized"));
  }

  @Test
  public void testDeleteWithInvalidToken() {
    UnauthorizedError read = helper.delete(url + "admin/user", createBearerHeaders("testing"),
        new ParameterizedTypeReference<UnauthorizedError>() {}, port).getBody();
    assertThat(read.getError(), is("invalid_token"));
  }
}
