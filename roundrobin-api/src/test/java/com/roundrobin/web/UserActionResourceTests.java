package com.roundrobin.web;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.api.UserActionTo;
import com.roundrobin.api.UserProfileTo;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.UserAction;
import com.roundrobin.domain.UserProfile;
import com.roundrobin.services.UserProfileService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by rengasu on 5/23/16.
 */
public class UserActionResourceTests extends ResourceTests {

  @Autowired
  protected UserProfileService userProfileService;

  @Test
  public void testActivate() {
    Response<UserProfileTo> userProfile = createUserProfile();
    UserProfile profile = userProfileService.get(userProfile.getEntity().getId());
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setId(profile.getActions().get(0).getId());
    userActionTo.setUserProfileId(profile.getId());
    userActionTo.setSecret(profile.getActions().get(0).getSecret());
    Response<Boolean> activate = helper.post(url +
                    "user-action/activate", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(activate.getEntity(), notNullValue());
    assertThat(activate.getEntity(), is(true));
  }

  @Test
  public void testDuplicateActivate() {
    Response<UserProfileTo> userProfile = createUserProfile();
    UserProfile profile = userProfileService.get(userProfile.getEntity().getId());
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setId(profile.getActions().get(0).getId());
    userActionTo.setUserProfileId(profile.getId());
    userActionTo.setSecret(profile.getActions().get(0).getSecret());
    Response<Boolean> activate = helper.post(url +
                    "user-action/activate", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(activate.getEntity(), notNullValue());
    assertThat(activate.getEntity(), is(true));
    activate = helper.post(url +
                    "user-action/activate", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(activate.getEntity(), nullValue());
    assertThat(activate.getErrors(), notNullValue());
    assertThat(activate.getErrors(), hasItems(
            new Error(ErrorCode.USER_ALREADY_ACTIVE.getCode(), messages.getErrorMessage(ErrorCode.USER_ALREADY_ACTIVE))
    ));
  }

  @Test
  public void testActivateWithEmptyValues() {
    Response<String> activate = helper.post(url +
                    "user-action/activate", new UserActionTo(),
            new ParameterizedTypeReference<Response<String>>() {
            }, port).getBody();
    assertThat(activate.getEntity(), nullValue());
    assertThat(activate.getErrors(), notNullValue());
    assertThat(activate.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "id: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "secret: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "userProfileId: may not be empty")
    ));
  }

  @Test
  public void testActivateWithInvalidActionId() {
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setId("testing");
    userActionTo.setUserProfileId("testing");
    userActionTo.setSecret("testing");
    Response<String> activate = helper.post(url +
                    "user-action/activate", userActionTo,
            new ParameterizedTypeReference<Response<String>>() {
            }, port).getBody();
    assertThat(activate.getEntity(), nullValue());
    assertThat(activate.getErrors(), notNullValue());
    assertThat(activate.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_USER_ACTION_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_USER_ACTION_ID))
    ));
  }

  @Test
  public void testActivateWithInvalidProfileId() {
    Response<UserProfileTo> userProfile = createUserProfile();
    UserProfile profile = userProfileService.get(userProfile.getEntity().getId());
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setId(profile.getActions().get(0).getId());
    userActionTo.setUserProfileId("testing");
    userActionTo.setSecret(profile.getActions().get(0).getSecret());
    Response<String> activate = helper.post(url +
                    "user-action/activate", userActionTo,
            new ParameterizedTypeReference<Response<String>>() {
            }, port).getBody();
    assertThat(activate.getEntity(), nullValue());
    assertThat(activate.getErrors(), notNullValue());
    assertThat(activate.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))
    ));
  }

  @Test
  public void testActivateWithInvalidSecretId() {
    Response<UserProfileTo> userProfile = createUserProfile();
    UserProfile profile = userProfileService.get(userProfile.getEntity().getId());
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setId(profile.getActions().get(0).getId());
    userActionTo.setUserProfileId(profile.getId());
    userActionTo.setSecret("testing");
    Response<String> activate = helper.post(url +
                    "user-action/activate", userActionTo,
            new ParameterizedTypeReference<Response<String>>() {
            }, port).getBody();
    assertThat(activate.getEntity(), nullValue());
    assertThat(activate.getErrors(), notNullValue());
    assertThat(activate.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_SECRET.getCode(), messages.getErrorMessage(ErrorCode.INVALID_SECRET))
    ));
  }

  @Test
  public void testActivateWithWrongProfileId() {
    Response<UserProfileTo> userProfile = createUserProfile();
    UserProfile profile = userProfileService.get(userProfile.getEntity().getId());
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setId(profile.getActions().get(0).getId());
    userActionTo.setUserProfileId(createUserProfile().getEntity().getId());
    userActionTo.setSecret(profile.getActions().get(0).getSecret());
    Response<Boolean> activate = helper.post(url +
                    "user-action/activate", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(activate.getEntity(), nullValue());
    assertThat(activate.getErrors(), notNullValue());
    assertThat(activate.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))
    ));
  }

  @Test
  public void testRequestActivate() {
    String email = System.currentTimeMillis() + "@testing.com";
    createUserProfile(email);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(email);
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-activate", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), notNullValue());
    assertThat(reset.getEntity(), is(true));
  }

  @Test
  public void testRequestActivateWithEmptyValues() {
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-activate", new UserActionTo(),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "email: may not be empty")
    ));
  }

  @Test
  public void testRequestActivateWithInvalidValues() {
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail("testing");
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-activate", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "email: not a well-formed email address")
    ));
  }

  @Test
  public void testRequestActivateWithUnknownEmail() {
    String email = System.currentTimeMillis() + "@testing.com";
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(email);
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-activate", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.UNKNOWN_PROFILE.getCode(), messages.getErrorMessage(ErrorCode.UNKNOWN_PROFILE))
    ));
  }

  @Test
  public void testRequestActivateForActiveProfile() {
    String email = System.currentTimeMillis() + "@testing.com";
    Response<UserProfileTo> userProfile = createUserProfile(email);
    UserProfile profile = userProfileService.get(userProfile.getEntity().getId());
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setId(profile.getActions().get(0).getId());
    userActionTo.setUserProfileId(profile.getId());
    userActionTo.setSecret(profile.getActions().get(0).getSecret());
    Response<Boolean> activate = helper.post(url +
                    "user-action/activate", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(activate.getEntity(), notNullValue());
    assertThat(activate.getEntity(), is(true));
    userActionTo = new UserActionTo();
    userActionTo.setEmail(profile.getEmail());
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-activate", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.USER_ALREADY_ACTIVE.getCode(), messages.getErrorMessage(ErrorCode.USER_ALREADY_ACTIVE))
    ));
  }

  @Test
  public void testRequestResetPassword() {
    String email = System.currentTimeMillis() + "@testing.com";
    createUserProfile(email);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(email);
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), notNullValue());
    assertThat(reset.getEntity(), is(true));
  }

  @Test
  public void testRequestResetPasswordWithEmptyValues() {
    UserActionTo userActionTo = new UserActionTo();
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "email: may not be empty")
    ));
  }

  @Test
  public void testRequestResetPasswordWithInvalidValues() {
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail("testing");
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "email: not a well-formed email address")
    ));
  }

  @Test
  public void testRequestResetPasswordWithUnknownEmail() {
    String email = System.currentTimeMillis() + "@testing.com";
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(email);
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.UNKNOWN_PROFILE.getCode(), messages.getErrorMessage(ErrorCode.UNKNOWN_PROFILE))
    ));
  }

  @Test
  public void testResetPassword() {
    String email = System.currentTimeMillis() + "@testing.com";
    Response<UserProfileTo> profile = createUserProfile(email);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(email);
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), notNullValue());
    assertThat(reset.getEntity(), is(true));
    UserProfile userProfile = userProfileService.get(profile.getEntity().getId());
    UserAction userAction = null;
    for (UserAction action : userProfile.getActions()) {
      if (action.getAction() == UserAction.UserActionType.RESET_PASSWORD) {
        userAction = action;
      }
    }
    userActionTo = new UserActionTo();
    userActionTo.setId(userAction.getId());
    userActionTo.setUserProfileId(profile.getEntity().getId());
    userActionTo.setSecret(userAction.getSecret());
    userActionTo.setPassword("newpassword");
    reset = helper.post(url +
                    "user-action/reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), notNullValue());
    assertThat(reset.getEntity(), is(true));
  }

  @Test
  public void testResetPasswordWithEmptyValues() {
    UserActionTo userActionTo = new UserActionTo();
    Response<Boolean> reset = helper.post(url +
                    "user-action/reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "id: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "secret: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "userProfileId: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "password: may not be empty")
    ));
  }

  @Test
  public void testResetPasswordWithInvalidActionId() {
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setId("testing");
    userActionTo.setUserProfileId("testing");
    userActionTo.setSecret("testing");
    userActionTo.setPassword("newpassword");
    Response<Boolean> reset = helper.post(url +
                    "user-action/reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_USER_ACTION_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_USER_ACTION_ID))
    ));
  }

  @Test
  public void testResetPasswordWithInvalidSecret() {
    String email = System.currentTimeMillis() + "@testing.com";
    Response<UserProfileTo> profile = createUserProfile(email);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(email);
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    UserProfile userProfile = userProfileService.get(profile.getEntity().getId());
    UserAction userAction = null;
    for (UserAction action : userProfile.getActions()) {
      if (action.getAction() == UserAction.UserActionType.RESET_PASSWORD) {
        userAction = action;
      }
    }
    userActionTo = new UserActionTo();
    userActionTo.setId(userAction.getId());
    userActionTo.setUserProfileId("testing");
    userActionTo.setSecret("testing");
    userActionTo.setPassword("newpassword");
    reset = helper.post(url +
                    "user-action/reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_SECRET.getCode(), messages.getErrorMessage(ErrorCode.INVALID_SECRET))
    ));
  }

  @Test
  public void testResetPasswordWithInvalidProfileId() {
    String email = System.currentTimeMillis() + "@testing.com";
    Response<UserProfileTo> profile = createUserProfile(email);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(email);
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    UserProfile userProfile = userProfileService.get(profile.getEntity().getId());
    UserAction userAction = null;
    for (UserAction action : userProfile.getActions()) {
      if (action.getAction() == UserAction.UserActionType.RESET_PASSWORD) {
        userAction = action;
      }
    }
    userActionTo = new UserActionTo();
    userActionTo.setId(userAction.getId());
    userActionTo.setUserProfileId("testing");
    userActionTo.setSecret(userAction.getSecret());
    userActionTo.setPassword("newpassword");
    reset = helper.post(url +
                    "user-action/reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))
    ));
  }

  @Test
  public void testResetPasswordWithWrongProfileId() {
    String email = System.currentTimeMillis() + "@testing.com";
    Response<UserProfileTo> profile = createUserProfile(email);
    UserActionTo userActionTo = new UserActionTo();
    userActionTo.setEmail(email);
    Response<Boolean> reset = helper.post(url +
                    "user-action/request-reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    UserProfile userProfile = userProfileService.get(profile.getEntity().getId());
    UserAction userAction = null;
    for (UserAction action : userProfile.getActions()) {
      if (action.getAction() == UserAction.UserActionType.RESET_PASSWORD) {
        userAction = action;
      }
    }
    userActionTo = new UserActionTo();
    userActionTo.setId(userAction.getId());
    userActionTo.setUserProfileId(createUserProfile().getEntity().getId());
    userActionTo.setSecret(userAction.getSecret());
    userActionTo.setPassword("newpassword");
    reset = helper.post(url +
                    "user-action/reset-password", userActionTo,
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, port).getBody();
    assertThat(reset.getEntity(), nullValue());
    assertThat(reset.getErrors(), notNullValue());
    assertThat(reset.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))
    ));
  }

}
