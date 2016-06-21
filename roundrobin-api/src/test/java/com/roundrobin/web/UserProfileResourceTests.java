package com.roundrobin.web;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.api.UserProfileTo;
import com.roundrobin.common.ErrorCode;
import com.roundrobin.domain.UserProfile;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by rengasu on 5/12/16.
 */
public class UserProfileResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("Suresh"));
    userProfileTo.setLastName(Optional.of("Rengasamy"));
    userProfileTo.setEmail(Optional.of("testing@testing.com"));
    userProfileTo.setMobileNumber(Optional.of("5555555555"));
    userProfileTo.setHomeNumber(Optional.of("5555555555"));
    userProfileTo.setVendor(Optional.of(false));
    userProfileTo.setPassword(Optional.of("testing"));
    userProfileTo.setDob(Optional.of(LocalDate.now()));
    userProfileTo.setSex(Optional.of(UserProfile.SexType.MALE));
    Response<UserProfileTo> created = helper.post(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<UserProfileTo>>() {
    }, port).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getFirstName().get(), is(userProfileTo.getFirstName().get()));
    assertThat(created.getEntity().getLastName().get(), is(userProfileTo.getLastName().get()));
    assertThat(created.getEntity().getMobileNumber().get(), is(userProfileTo.getMobileNumber().get()));
    assertThat(created.getEntity().getHomeNumber().get(), is(userProfileTo.getHomeNumber().get()));
    assertThat(created.getEntity().getVendor().get(), is(userProfileTo.getVendor().get()));
    assertThat(created.getEntity().getDob().get(), is(userProfileTo.getDob().get()));
    assertThat(created.getEntity().getSex().get(), is(userProfileTo.getSex().get()));
  }

  @Test
  public void testCreateWithEmptyValue() {
    UserProfileTo userProfileTo = new UserProfileTo();
    Response<String> created = helper.post(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "firstName: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "lastName: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "email: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "password: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "vendor: may not be null"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "mobileNumber: may not be empty")));
  }

  @Test
  public void testCreateWithInvalidValue() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("email", "testing");
    userProfileTo.put("mobileNumber", "testing");
    userProfileTo.put("homeNumber", "testing");
    Response<String> created = helper.post(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "mobileNumber: must match \"(^$|[0-9]{10})\""),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "homeNumber: must match \"(^$|[0-9]{10})\""),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "email: not a well-formed email address")
    ));
  }

  @Test
  public void testCreateWithInvalidVendor() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("vendor", "testing");
    Response<String> created = helper.post(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.UNPARSABLE_INPUT.getCode(), messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testCreateWithInvalidDob() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("dob", "testing");
    Response<String> created = helper.post(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.UNPARSABLE_INPUT.getCode(), messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testCreateWithInvalidSex() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("sex", "testing");
    Response<String> created = helper.post(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.UNPARSABLE_INPUT.getCode(), messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testCreateWithInvalidLength() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("aaaaaaaaaadasdajshdasjdaskdasdjasdasdasdasdasdasdasdasdasd"));
    userProfileTo.setLastName(Optional.of("aaaaaaaaaadasdajshdasjdaskdasdjasdasdasdasdasdasdasdasdasd"));
    userProfileTo.setPassword(Optional.of("aaaaaaaaaadasdajshdasjdaskdasdjasdasdasdasdasdasdasdasdasd"));
    Response<String> created = helper.post(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "lastName: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "firstName: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "password: length must be between 0 and 35")));
  }

  @Test
  public void testUpdateProfile() {
    Response<UserProfileTo> userProfileTo = createUserProfile();
    UserProfileTo created = userProfileTo.getEntity();
    created.setFirstName(Optional.of("testing"));
    created.setLastName(Optional.of("testing"));
    created.setMobileNumber(Optional.of("1111111111"));
    created.setHomeNumber(Optional.of("1111111111"));
    created.setSex(Optional.of(UserProfile.SexType.FEMALE));
    created.setDob(Optional.of(LocalDate.now().minusDays(1000)));
    created.setVendor(Optional.of(true));
    Response<UserProfileTo> updated = helper.put(url + "user-profile", created, new ParameterizedTypeReference<Response<UserProfileTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getEntity().getFirstName().get(), is(created.getFirstName().get()));
    assertThat(updated.getEntity().getLastName().get(), is(created.getLastName().get()));
    assertThat(updated.getEntity().getMobileNumber().get(), is(created.getMobileNumber().get()));
    assertThat(updated.getEntity().getHomeNumber().get(), is(created.getHomeNumber().get()));
    assertThat(updated.getEntity().getVendor().get(), is(created.getVendor().get()));
    assertThat(updated.getEntity().getDob().get(), is(created.getDob().get()));
    assertThat(updated.getEntity().getSex().get(), is(created.getSex().get()));
  }

  @Test
  public void testUpdateWithEmptyValue() {
    UserProfileTo userProfileTo = new UserProfileTo();
    Response<String> updated = helper.put(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "id: may not be empty")
    ));
  }

  @Test
  public void testUpdateWithInvalidValue() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("asdsahjdhasjdhajshdjashdsahfjsdagfhjdgjfahsgdfkashdfgkhsagdfkhahsgdf"));
    userProfileTo.setLastName(Optional.of("asdsahjdhasjdhajshdjashdsahfjsdagfhjdgjfahsgdfkashdfgkhsagdfkhahsgdf"));
    userProfileTo.setMobileNumber(Optional.of("testing"));
    userProfileTo.setHomeNumber(Optional.of("testing"));
    Response<String> updated = helper.put(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_FIELD.getCode(), "lastName: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "firstName: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "mobileNumber: must match \"(^$|[0-9]{10})\""),
            new Error(ErrorCode.INVALID_FIELD.getCode(), "homeNumber: must match \"(^$|[0-9]{10})\"")
    ));
  }

  @Test
  public void testUpdateWithInvalidProfileId() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setId("testing");
    Response<String> updated = helper.put(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))
    ));
  }

  @Test
  public void testUpdateWithInvalidSex() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("sex", "testing");
    Response<String> updated = helper.put(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.UNPARSABLE_INPUT.getCode(), messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))
    ));
  }

  @Test
  public void testUpdateWithInvalidVendor() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("vendor", "testing");
    Response<String> updated = helper.put(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.UNPARSABLE_INPUT.getCode(), messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))
    ));
  }

  @Test
  public void testUpdateWithInvalidDob() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("dob", "testing");
    Response<String> updated = helper.put(url + "user-profile", userProfileTo, new ParameterizedTypeReference<Response<String>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
            new Error(ErrorCode.UNPARSABLE_INPUT.getCode(), messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))
    ));
  }

  @Test
  public void testRead() {
    Response<UserProfileTo> created = createUserProfile();
    Response<UserProfileTo> read = helper.get(url + "user-profile/{userProfileId}", new ParameterizedTypeReference<Response<UserProfileTo>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getFirstName().get(), is(created.getEntity().getFirstName().get()));
    assertThat(read.getEntity().getLastName().get(), is(created.getEntity().getLastName().get()));
    assertThat(read.getEntity().getMobileNumber().get(), is(created.getEntity().getMobileNumber().get()));
    assertThat(read.getEntity().getHomeNumber().get(), is(created.getEntity().getHomeNumber().get()));
    assertThat(read.getEntity().getVendor().get(), is(created.getEntity().getVendor().get()));
    assertThat(read.getEntity().getDob().get(), is(created.getEntity().getDob().get()));
    assertThat(read.getEntity().getSex().get(), is(created.getEntity().getSex().get()));
  }

  @Test
  public void testReadWithEmptyId() {
    Response<String> read = helper.get(url + "user-profile/{userProfileId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, "").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_URL.getCode(), messages.getErrorMessage(ErrorCode.INVALID_URL))
    ));
  }

  @Test
  public void testReadWithInvalidId() {
    Response<String> read = helper.get(url + "user-profile/{userProfileId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))
    ));
  }

  @Test
  public void testDelete() {
    Response<UserProfileTo> created = createUserProfile();
    Response<Boolean> deleted = helper.delete(url + "user-profile/{userProfileId}", new ParameterizedTypeReference<Response<Boolean>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<String> read = helper.delete(url + "user-profile/{userProfileId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, "").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_URL.getCode(), messages.getErrorMessage(ErrorCode.INVALID_URL))
    ));
  }

  @Test
  public void testDeleteWithInvalidId() {
    Response<String> read = helper.delete(url + "user-profile/{userProfileId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), hasItems(
            new Error(ErrorCode.INVALID_PROFILE_ID.getCode(), messages.getErrorMessage(ErrorCode.INVALID_PROFILE_ID))
    ));
  }
}
