package com.roundrobin.vault.web;


import com.roundrobin.core.api.Response;
import com.roundrobin.test.api.UnauthorizedError;
import com.roundrobin.vault.ResourceTests;
import com.roundrobin.vault.api.UserProfileTo;
import com.roundrobin.vault.enums.SexType;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.roundrobin.core.common.ErrorCodes.INVALID_FIELD;
import static com.roundrobin.core.common.ErrorCodes.UNKNOWN_MEDIATYPE;
import static com.roundrobin.core.common.ErrorCodes.UNPARSABLE_INPUT;
import static com.roundrobin.core.common.ErrorTypes.INVALID_REQUEST_ERROR;
import static com.roundrobin.core.common.ErrorCodes.INVALID_USER_ID;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class UserProfileResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("Suresh"));
    userProfileTo.setLastName(Optional.of("Rengasamy"));
    userProfileTo.setMobileNumber(Optional.of("5555555555"));
    userProfileTo.setHomeNumber(Optional.of("5555555555"));
    userProfileTo.setVendor(Optional.of(false));
    userProfileTo.setDob(Optional.of(LocalDate.now()));
    userProfileTo.setSex(Optional.of(SexType.MALE));
    Response<UserProfileTo> created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
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
    Response<String> created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(),
            hasItems("firstName: may not be empty",
                    "lastName: may not be empty",
                    "vendor: may not be null",
                    "mobileNumber: may not be empty"));
  }

  @Test
  public void testCreateWithInvalidValue() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("mobile_number", "testing");
    userProfileTo.put("home_number", "testing");
    userProfileTo.put("first_name", "asdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasd");
    userProfileTo.put("last_name", "asdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasdasd");
    Response<String> created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(),
            hasItems("mobileNumber: must match \"(^$|[0-9]{10})\"",
                    "homeNumber: must match \"(^$|[0-9]{10})\"",
                    "firstName: length must be between 0 and 35",
                    "lastName: length must be between 0 and 35"));
  }

  @Test
  public void testCreateWithInvalidVendor() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("vendor", "testing");
    Response<String> created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }


  @Test
  public void testCreateWithInvalidDob() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("dob", "testing");
    Response<String> created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testCreateWithInvalidSex() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("sex", "testing");
    Response<String> created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testCreateWithInvalidContentType() {
    Response<String> created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(MediaType.APPLICATION_XML, "", createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNKNOWN_MEDIATYPE));
    assertThat(created.getError().getMessage(), is("Unknown media type"));
  }

  @Test
  public void testCreateWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testCreateWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testCreateWithInvalidScope() {
    String token = getMockAccessToken(UUID.randomUUID().toString(), (System.currentTimeMillis() + 120000) / 1000,
            getInvalidScopes(), getValidResources());
    UserProfileTo userProfileTo = new UserProfileTo();
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(token)),
            UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("insufficient_scope"));
    assertThat(created.getDescription(), is("Insufficient scope for this resource"));
  }


  @Test
  public void testCreateWithInvalidResource() {
    String token = getMockAccessToken(UUID.randomUUID().toString(), (System.currentTimeMillis() + 120000) / 1000,
            getValidScopes(), getInvalidResources());
    UserProfileTo userProfileTo = new UserProfileTo();
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(token)),
            UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("access_denied"));
    assertThat(created.getDescription(), is("Invalid token does not contain resource id (web)"));
  }

  @Test
  public void testCreateWithExpiredToken() {
    String token = getMockAccessToken(UUID.randomUUID().toString(), (System.currentTimeMillis() - 120000) / 1000,
            getValidScopes(), getValidResources());
    UserProfileTo userProfileTo = new UserProfileTo();
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(token)),
            UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
    assertThat(created.getDescription(), is("Access token expired: " + token));
  }

  @Test
  public void testUpdateProfile() {
    String userId = createUserProfile();
    UserProfileTo created = new UserProfileTo();
    created.setFirstName(Optional.of("testing"));
    created.setLastName(Optional.of("testing"));
    created.setMobileNumber(Optional.of("1111111111"));
    created.setHomeNumber(Optional.of("1111111111"));
    created.setSex(Optional.of(SexType.FEMALE));
    created.setDob(Optional.of(LocalDate.now().minusDays(1000)));
    created.setVendor(Optional.of(true));
    Response<UserProfileTo> updated =
            template.exchange(vaultUrl + "user-profile", HttpMethod.PUT,
                    createHttpEntity(created, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<UserProfileTo>>() {
                    }).getBody();
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
    String userId = createUserProfile();
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setFirstName(Optional.of("asdsahjdhasjdhajshdjashdsahfjsdagfhjdgjfahsgdfkashdfgkhsagdfkhahsgdf"));
    userProfileTo.setLastName(Optional.of("asdsahjdhasjdhajshdjashdsahfjsdagfhjdgjfahsgdfkashdfgkhsagdfkhahsgdf"));
    userProfileTo.setMobileNumber(Optional.of("testing"));
    userProfileTo.setHomeNumber(Optional.of("testing"));
    Response<String> updated = template.exchange(vaultUrl + "user-profile", HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(),
            hasItems("lastName: length must be between 0 and 35",
                    "firstName: length must be between 0 and 35",
                    "mobileNumber: must match \"(^$|[0-9]{10})\"",
                    "homeNumber: must match \"(^$|[0-9]{10})\""));
  }

  @Test
  public void testUpdateWithInvalidSex() {
    String userId = createUserProfile();
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("sex", "testing");
    Response<String> updated = template.exchange(vaultUrl + "user-profile", HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(updated.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidVendor() {
    String userId = createUserProfile();
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("vendor", "testing");
    Response<String> updated = template.exchange(vaultUrl + "user-profile", HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(updated.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidDob() {
    String userId = createUserProfile();
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("dob", "testing");
    Response<String> updated = template.exchange(vaultUrl + "user-profile", HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(updated.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.PUT,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testUpdateWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.PUT,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testRead() {
    String userId = createUserProfile();
    Response<UserProfileTo> read = template.exchange(vaultUrl + "user-profile", HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
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
  public void testReadWithoutProfile() {
    Response<UserProfileTo> read = template.exchange(vaultUrl + "user-profile", HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(UUID.randomUUID().toString()))),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_USER_ID));
    assertThat(read.getError().getMessage(), is("Invalid user id"));
  }

  @Test
  public void testReadWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.GET,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testReadWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testDelete() {
    String userId = createUserProfile();
    Response<Boolean> deleted = template.exchange(vaultUrl + "user-profile", HttpMethod.DELETE,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
  }

  @Test
  public void testDeleteWithEmptyToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.DELETE,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testDeleteWithInvalidToken() {
    UnauthorizedError created = template.exchange(vaultUrl + "user-profile",
            HttpMethod.DELETE,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }
}
