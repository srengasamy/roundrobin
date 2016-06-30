package com.roundrobin.web;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.vault.api.UserProfileTo;
import com.roundrobin.vault.common.ErrorCode;
import com.roundrobin.vault.domain.UserProfile;
import com.roundrobin.vault.domain.UserProfile.SexType;

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
    Response<UserProfileTo> created = helper.post(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<UserProfileTo>>() {}).getBody();
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
    Response<String> created = helper.post(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "firstName: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "lastName: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "email: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "password: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "vendor: may not be null"),
            new Error(ErrorCode.INVALID_FIELD, "mobileNumber: may not be empty")));
  }

  @Test
  public void testCreateWithInvalidValue() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("email", "testing");
    userProfileTo.put("mobileNumber", "testing");
    userProfileTo.put("homeNumber", "testing");
    Response<String> created = helper.post(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "mobileNumber: must match \"(^$|[0-9]{10})\""),
            new Error(ErrorCode.INVALID_FIELD, "homeNumber: must match \"(^$|[0-9]{10})\""),
            new Error(ErrorCode.INVALID_FIELD, "email: not a well-formed email address")));
  }

  @Test
  public void testCreateWithInvalidVendor() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("vendor", "testing");
    Response<String> created = helper.post(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testCreateWithInvalidDob() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("dob", "testing");
    Response<String> created = helper.post(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testCreateWithInvalidSex() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("sex", "testing");
    Response<String> created = helper.post(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testCreateWithInvalidLength() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("aaaaaaaaaadasdajshdasjdaskdasdjasdasdasdasdasdasdasdasdasd"));
    userProfileTo.setLastName(Optional.of("aaaaaaaaaadasdajshdasjdaskdasdjasdasdasdasdasdasdasdasdasd"));
    userProfileTo.setPassword(Optional.of("aaaaaaaaaadasdajshdasjdaskdasdjasdasdasdasdasdasdasdasdasd"));
    Response<String> created = helper.post(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "lastName: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD, "firstName: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD, "password: length must be between 0 and 35")));
  }

  @Test
  public void testUpdateProfile() {
    String username = createUserProfile();
    UserProfileTo created = new UserProfileTo();
    created.setFirstName(Optional.of("testing"));
    created.setLastName(Optional.of("testing"));
    created.setMobileNumber(Optional.of("1111111111"));
    created.setHomeNumber(Optional.of("1111111111"));
    created.setSex(Optional.of(UserProfile.SexType.FEMALE));
    created.setDob(Optional.of(LocalDate.now().minusDays(1000)));
    created.setVendor(Optional.of(true));
    Response<UserProfileTo> updated =
        helper.put(vaultUrl + "user-profile", createBearerHeaders(getAccessToken(username)), created,
            new ParameterizedTypeReference<Response<UserProfileTo>>() {}).getBody();
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
  public void testUpdateWithInvalidValue() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("asdsahjdhasjdhajshdjashdsahfjsdagfhjdgjfahsgdfkashdfgkhsagdfkhahsgdf"));
    userProfileTo.setLastName(Optional.of("asdsahjdhasjdhajshdjashdsahfjsdagfhjdgjfahsgdfkashdfgkhsagdfkhahsgdf"));
    userProfileTo.setMobileNumber(Optional.of("testing"));
    userProfileTo.setHomeNumber(Optional.of("testing"));
    Response<String> updated = helper.put(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "lastName: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD, "firstName: length must be between 0 and 35"),
            new Error(ErrorCode.INVALID_FIELD, "mobileNumber: must match \"(^$|[0-9]{10})\""),
            new Error(ErrorCode.INVALID_FIELD, "homeNumber: must match \"(^$|[0-9]{10})\"")));
  }

  @Test
  public void testUpdateWithInvalidSex() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("sex", "testing");
    Response<String> updated = helper.put(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(),
        hasItems(new Error(ErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testUpdateWithInvalidVendor() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("vendor", "testing");
    Response<String> updated = helper.put(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(),
        hasItems(new Error(ErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testUpdateWithInvalidDob() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("dob", "testing");
    Response<String> updated = helper.put(vaultUrl + "user-profile", createBearerHeaders(), userProfileTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(),
        hasItems(new Error(ErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testRead() {
    String username = createUserProfile();
    Response<UserProfileTo> read = helper.get(vaultUrl + "user-profile", createBearerHeaders(getAccessToken(username)),
        new ParameterizedTypeReference<Response<UserProfileTo>>() {}).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getFirstName().get(), is("Suresh"));
    assertThat(read.getEntity().getLastName().get(), is("Rengasamy"));
    assertThat(read.getEntity().getMobileNumber().get(), is("5555555555"));
    assertThat(read.getEntity().getHomeNumber().get(), is("5555555555"));
    assertThat(read.getEntity().getVendor().get(), is(false));
    assertThat(read.getEntity().getDob().get(), is(LocalDate.now()));
    assertThat(read.getEntity().getSex().get(), is(SexType.MALE));
  }

  @Test
  public void testDelete() {
    String username = createUserProfile();
    Response<Boolean> deleted = helper.delete(vaultUrl + "user-profile", createBearerHeaders(getAccessToken(username)),
        new ParameterizedTypeReference<Response<Boolean>>() {}).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
  }

}
