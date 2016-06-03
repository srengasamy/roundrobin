package com.roundrobin.web;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.api.UserActionTo;
import com.roundrobin.api.UserTo;
import com.roundrobin.common.CommonErrorCode;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.User;
import com.roundrobin.service.UserService;

/**
 * Created by rengasu on 5/12/16.
 */
public class UserActionResourceTests extends ResourceTests {
  // TODO Include all test cases
  // TODO Generate UUID
  // TODO Messages for all error code
  // TODO Handle all other api failures like 404/400
  // TODO Complete pom file
  // TODO Add indexes to columns
  // TODO Store token in db
  // TODO Handle 404 error

  @Autowired
  private UserService userService;

  @Test
  public void testCreate() {
    UserTo userTo = new UserTo();
    userTo.setUsername(Optional.of("testing" + System.currentTimeMillis() + "@testing.com"));
    userTo.setVendor(Optional.of(false));
    userTo.setPassword(Optional.of("testing"));
    Response<Boolean> created = helper
        .post(url + "user-action/create-user", userTo, new ParameterizedTypeReference<Response<Boolean>>() {}, port)
        .getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity(), is(true));
  }

  @Test
  public void testCreateWithEmptyValue() {
    UserTo userProfileTo = new UserTo();
    Response<String> created = helper.post(url + "user-action/create-user", userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(CommonErrorCode.INVALID_FIELD, "username: may not be empty"),
            new Error(CommonErrorCode.INVALID_FIELD, "password: may not be empty"),
            new Error(CommonErrorCode.INVALID_FIELD, "vendor: may not be null")));
  }

  @Test
  public void testCreateWithInvalidValue() {
    Map<String, String> userTo = new HashMap<>();
    userTo.put("username", "testing");
    userTo.put("password", "testingdaskdjaskjdkasjdkjaskdjkasjdkasjdkasjdkasjdkajsdkjaksdjkasdjkajs");
    Response<String> created = helper
        .post(url + "user-action/create-user", userTo, new ParameterizedTypeReference<Response<String>>() {}, port)
        .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(CommonErrorCode.INVALID_FIELD, "vendor: may not be null"),
            new Error(CommonErrorCode.INVALID_FIELD, "password: length must be between 0 and 35"),
            new Error(CommonErrorCode.INVALID_FIELD, "username: not a well-formed email address")));
  }

  @Test
  public void testCreateWithInvalidVendor() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("vendor", "testing");
    Response<String> created = helper.post(url + "user-action/create-user", userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}, port).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
        new Error(CommonErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(CommonErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testCreateWithExistingName() {
    String username = createUser();
    UserTo userTo = new UserTo();
    userTo.setUsername(Optional.of(username));
    userTo.setVendor(Optional.of(false));
    userTo.setPassword(Optional.of("testing"));
    Response<String> created = helper
        .post(url + "user-action/create-user", userTo, new ParameterizedTypeReference<Response<String>>() {}, port)
        .getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.USER_ALREADY_EXIST, messages.getErrorMessage(ErrorCode.USER_ALREADY_EXIST))));
  }

  @Test
  public void testCreateWithEmptyJson() {
    Response<String> created =
        helper.post(url + "user-action/create-user", "", new ParameterizedTypeReference<Response<String>>() {}, port)
            .getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
        new Error(CommonErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(CommonErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testVerify() {
    String username = createUser();
    User user = userService.getByUsername(username);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setUserId(user.getId());
    userActionTo.setSecret(user.getActions().get(0).getSecret());
    Response<Boolean> verify = helper
        .post(url + "user-action/verify", userActionTo, new ParameterizedTypeReference<Response<Boolean>>() {}, port)
        .getBody();
    assertThat(verify.getEntity(), notNullValue());
    assertThat(verify.getEntity(), is(true));
  }

  @Test
  public void testDuplicateVerify() {
    String username = createUser();
    User user = userService.getByUsername(username);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setUserId(user.getId());
    userActionTo.setSecret(user.getActions().get(0).getSecret());
    Response<Boolean> verify = helper
        .post(url + "user-action/verify", userActionTo, new ParameterizedTypeReference<Response<Boolean>>() {}, port)
        .getBody();
    assertThat(verify.getEntity(), notNullValue());
    assertThat(verify.getEntity(), is(true));
    verify = helper
        .post(url + "user-action/verify", userActionTo, new ParameterizedTypeReference<Response<Boolean>>() {}, port)
        .getBody();
    assertThat(verify.getEntity(), nullValue());
    assertThat(verify.getErrors(), notNullValue());
    assertThat(verify.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_SECRET, messages.getErrorMessage(ErrorCode.INVALID_SECRET))));
  }

  @Test
  public void testVerifyWithEmptyValues() {
    Response<String> verify = helper.post(url + "user-action/verify", new UserActionTo(),
        new ParameterizedTypeReference<Response<String>>() {}, port).getBody();
    assertThat(verify.getEntity(), nullValue());
    assertThat(verify.getErrors(), notNullValue());
    assertThat(verify.getErrors(), hasItems(new Error(CommonErrorCode.INVALID_FIELD, "secret: may not be empty"),
        new Error(CommonErrorCode.INVALID_FIELD, "userId: may not be empty")));
  }

  @Test
  public void testVerifyWithInvalidProfileId() {
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setUserId("testing");
    userActionTo.setSecret("testing");
    Response<String> verify = helper
        .post(url + "user-action/verify", userActionTo, new ParameterizedTypeReference<Response<String>>() {}, port)
        .getBody();
    assertThat(verify.getEntity(), nullValue());
    assertThat(verify.getErrors(), notNullValue());
    assertThat(verify.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_USER_ID, messages.getErrorMessage(ErrorCode.INVALID_USER_ID))));
  }

  @Test
  public void testVerifyWithInvalidSecretId() {
    String username = createUser();
    User user = userService.getByUsername(username);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setUserId(user.getId());
    userActionTo.setSecret("testing");
    Response<Boolean> verify = helper
        .post(url + "user-action/verify", userActionTo, new ParameterizedTypeReference<Response<Boolean>>() {}, port)
        .getBody();
    assertThat(verify.getEntity(), nullValue());
    assertThat(verify.getErrors(), notNullValue());
    assertThat(verify.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_SECRET, messages.getErrorMessage(ErrorCode.INVALID_SECRET))));
  }

  @Test
  public void testVerifyWithWrongProfileId() {
    String username = createUser();
    User user = userService.getByUsername(username);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setUserId(userService.getByUsername(createUser()).getId());
    userActionTo.setSecret(user.getActions().get(0).getSecret());
    Response<Boolean> verify = helper
        .post(url + "user-action/verify", userActionTo, new ParameterizedTypeReference<Response<Boolean>>() {}, port)
        .getBody();
    assertThat(verify.getEntity(), nullValue());
    assertThat(verify.getErrors(), notNullValue());
    assertThat(verify.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_SECRET, messages.getErrorMessage(ErrorCode.INVALID_SECRET))));
  }

  @Test
  public void testRequestVerify() {
    String username = createUser();
    User user = userService.getByUsername(username);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(user.getUsername());
    Response<Boolean> requestVerify = helper.post(url + "user-action/request-verify", userActionTo,
        new ParameterizedTypeReference<Response<Boolean>>() {}, port).getBody();
    assertThat(requestVerify.getEntity(), notNullValue());
    assertThat(requestVerify.getEntity(), is(true));
  }

  @Test
  public void testRequestVerifyWithEmptyValues() {
    Response<Boolean> verify = helper.post(url + "user-action/request-verify", new UserActionTo(),
        new ParameterizedTypeReference<Response<Boolean>>() {}, port).getBody();
    assertThat(verify.getEntity(), nullValue());
    assertThat(verify.getErrors(), notNullValue());
    assertThat(verify.getErrors(), hasItems(new Error(CommonErrorCode.INVALID_FIELD, "email: may not be empty")));
  }

  @Test
  public void testRequestVerifyWithInvalidValues() {
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail("testing");
    Response<Boolean> verify = helper.post(url + "user-action/request-verify", userActionTo,
        new ParameterizedTypeReference<Response<Boolean>>() {}, port).getBody();
    assertThat(verify.getEntity(), nullValue());
    assertThat(verify.getErrors(), notNullValue());
    assertThat(verify.getErrors(),
        hasItems(new Error(CommonErrorCode.INVALID_FIELD, "email: not a well-formed email address")));
  }

  @Test
  public void testRequestVerifyWithUnknownEmail() {
    String email = System.currentTimeMillis() + "@testing.com";
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(email);
    Response<Boolean> verify = helper.post(url + "user-action/request-verify", userActionTo,
        new ParameterizedTypeReference<Response<Boolean>>() {}, port).getBody();
    assertThat(verify.getEntity(), nullValue());
    assertThat(verify.getErrors(), notNullValue());
    assertThat(verify.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_USERNAME, messages.getErrorMessage(ErrorCode.INVALID_USERNAME))));
  }

  @Test
  public void testRequestVerifyForVerifiedProfile() {
    String username = createUser();
    User user = userService.getByUsername(username);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setUserId(user.getId());
    userActionTo.setSecret(user.getActions().get(0).getSecret());
    Response<Boolean> verify = helper
        .post(url + "user-action/verify", userActionTo, new ParameterizedTypeReference<Response<Boolean>>() {}, port)
        .getBody();
    assertThat(verify.getEntity(), notNullValue());
    assertThat(verify.getEntity(), is(true));
    userActionTo = new UserActionTo();
    userActionTo.setEmail(user.getUsername());
    Response<Boolean> requestVerify = helper.post(url + "user-action/request-verify", userActionTo,
        new ParameterizedTypeReference<Response<Boolean>>() {}, port).getBody();
    assertThat(requestVerify.getEntity(), nullValue());
    assertThat(requestVerify.getErrors(), notNullValue());
    assertThat(requestVerify.getErrors(), hasItems(
        new Error(ErrorCode.USER_ALREADY_VERIFIED, messages.getErrorMessage(ErrorCode.USER_ALREADY_VERIFIED))));
  }
}
