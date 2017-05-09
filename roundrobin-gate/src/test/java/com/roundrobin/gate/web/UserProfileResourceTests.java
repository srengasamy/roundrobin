package com.roundrobin.gate.web;


import com.roundrobin.core.api.Response;
import com.roundrobin.gate.ResourceTests;
import com.roundrobin.gate.api.UserProfileTo;
import com.roundrobin.test.api.UnauthorizedError;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.roundrobin.core.common.ErrorCodes.INVALID_FIELD;
import static com.roundrobin.core.common.ErrorCodes.UNPARSABLE_INPUT;
import static com.roundrobin.core.common.ErrorTypes.INVALID_REQUEST_ERROR;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_CLOCK_TIME;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class UserProfileResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setClockIn((short) 0);
    userProfileTo.setClockOut((short) 1);
    userProfileTo.setLocation(new GeoJsonPoint(0, 0));
    userProfileTo.setOvertime((short) 15);
    userProfileTo.setRadius((short) 5);
    Response<UserProfileTo> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getError(), nullValue());
    assertThat(created.getEntity().getClockIn(), is(userProfileTo.getClockIn()));
    assertThat(created.getEntity().getClockOut(), is(userProfileTo.getClockOut()));
    assertThat(created.getEntity().getLocation().getX(), is(userProfileTo.getLocation().getX()));
    assertThat(created.getEntity().getLocation().getY(), is(userProfileTo.getLocation().getY()));
    assertThat(created.getEntity().getOvertime(), is(userProfileTo.getOvertime()));
    assertThat(created.getEntity().getRadius(), is(userProfileTo.getRadius()));
  }

  @Test
  public void testDoubleCreate() {
    String userId = createUserProfile();
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setClockIn((short) 1);
    userProfileTo.setClockOut((short) 2);
    userProfileTo.setLocation(new GeoJsonPoint(3, 3));
    userProfileTo.setOvertime((short) 16);
    userProfileTo.setRadius((short) 6);
    Response<UserProfileTo> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getError(), nullValue());
    assertThat(created.getEntity().getClockIn(), is(userProfileTo.getClockIn()));
    assertThat(created.getEntity().getClockOut(), is(userProfileTo.getClockOut()));
    assertThat(created.getEntity().getLocation().getX(), is(userProfileTo.getLocation().getX()));
    assertThat(created.getEntity().getLocation().getY(), is(userProfileTo.getLocation().getY()));
    assertThat(created.getEntity().getOvertime(), is(userProfileTo.getOvertime()));
    assertThat(created.getEntity().getRadius(), is(userProfileTo.getRadius()));
  }

  @Test
  public void testCreateWithEmptyValue() {
    UserProfileTo userProfileTo = new UserProfileTo();
    Response<String> created = template.exchange(gateUrl + "user-profile",
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
            hasItems("overtime: may not be null",
                    "radius: may not be null",
                    "clockIn: may not be null",
                    "clockOut: may not be null",
                    "location: may not be null"));
  }

  @Test
  public void testCreateWithInvalidValue() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("clock_in", "1500");
    userProfileTo.put("clock_out", "1500");
    userProfileTo.put("overtime", "1500");
    userProfileTo.put("radius", "1500");
    Response<String> created = template.exchange(gateUrl + "user-profile",
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
            hasItems("overtime: must be between 15 and 1440",
                    "radius: must be between 5 and 100",
                    "clockIn: must be between 0 and 1440",
                    "clockOut: must be between 0 and 1440",
                    "location: may not be null"));
  }

  @Test
  public void testCreateWithInvalidLocation() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("location", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
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
  public void testCreateWithInvalidClockIn() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("clock_in", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
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
  public void testCreateWithInvalidClockOut() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("clock_out", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
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
  public void testCreateWithInvalidOvertime() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("overtime", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
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
  public void testCreateWithInvalidRadius() {
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("radius", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
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
  public void testCreateWithInvalidClockTime() {
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setClockIn((short) 10);
    userProfileTo.setClockOut((short) 9);
    userProfileTo.setLocation(new GeoJsonPoint(0, 0));
    userProfileTo.setOvertime((short) 15);
    userProfileTo.setRadius((short) 5);
    Response<String> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_CLOCK_TIME));
    assertThat(created.getError().getMessage(), is("Invalid clock in/out time. Clock in must be less than clock out."));
  }

  @Test
  public void testCreateWithEmptyToken() {
    UnauthorizedError created = template.exchange(gateUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testCreateWithInvalidToken() {
    UnauthorizedError created = template.exchange(gateUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testCreateWithInvalidScope() {
    String token = getMockAccessToken(UUID.randomUUID().toString(), (System.currentTimeMillis() + 120001) / 1000,
            getInvalidScopes(), getValidResources());
    UserProfileTo userProfileTo = new UserProfileTo();
    UnauthorizedError created = template.exchange(gateUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(token)),
            UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("insufficient_scope"));
    assertThat(created.getDescription(), is("Insufficient scope for this resource"));
  }


  @Test
  public void testCreateWithInvalidResource() {
    String token = getMockAccessToken(UUID.randomUUID().toString(), (System.currentTimeMillis() + 120001) / 1000,
            getValidScopes(), getInvalidResources());
    UserProfileTo userProfileTo = new UserProfileTo();
    UnauthorizedError created = template.exchange(gateUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(token)),
            UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("access_denied"));
    assertThat(created.getDescription(), is("Invalid token does not contain resource id (gate)"));
  }

  @Test
  public void testCreateWithExpiredToken() {
    String token = getMockAccessToken(UUID.randomUUID().toString(), (System.currentTimeMillis() - 120001) / 1000,
            getValidScopes(), getValidResources());
    UserProfileTo userProfileTo = new UserProfileTo();
    UnauthorizedError created = template.exchange(gateUrl + "user-profile",
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
    created.setClockIn((short) 5);
    created.setClockOut((short) 10);
    created.setLocation(new GeoJsonPoint(1, 1));
    created.setOvertime((short) 16);
    created.setRadius((short) 10);
    Response<UserProfileTo> updated =
            template.exchange(gateUrl + "user-profile", HttpMethod.PUT,
                    createHttpEntity(created, createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<UserProfileTo>>() {
                    }).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getError(), nullValue());
    assertThat(updated.getEntity().getClockIn(), is(created.getClockIn()));
    assertThat(updated.getEntity().getClockOut(), is(created.getClockOut()));
    assertThat(updated.getEntity().getLocation(), is(created.getLocation()));
    assertThat(updated.getEntity().getOvertime(), is(created.getOvertime()));
    assertThat(updated.getEntity().getRadius(), is(created.getRadius()));
  }

  @Test
  public void testUpdateProfileWithoutCreate() {
    UserProfileTo created = new UserProfileTo();
    created.setClockIn((short) 5);
    created.setClockOut((short) 10);
    created.setLocation(new GeoJsonPoint(1, 1));
    created.setOvertime((short) 16);
    created.setRadius((short) 10);
    Response<UserProfileTo> updated =
            template.exchange(gateUrl + "user-profile", HttpMethod.PUT,
                    createHttpEntity(created, createBearerHeader(getMockAccessToken())),
                    new ParameterizedTypeReference<Response<UserProfileTo>>() {
                    }).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getError(), nullValue());
    assertThat(updated.getEntity().getClockIn(), is(created.getClockIn()));
    assertThat(updated.getEntity().getClockOut(), is(created.getClockOut()));
    assertThat(updated.getEntity().getLocation(), is(created.getLocation()));
    assertThat(updated.getEntity().getOvertime(), is(created.getOvertime()));
    assertThat(updated.getEntity().getRadius(), is(created.getRadius()));
  }

  @Test
  public void testUpdateWithInvalidValue() {
    String userId = createUserProfile();
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("clock_in", "1500");
    userProfileTo.put("clock_out", "1500");
    userProfileTo.put("overtime", "1500");
    userProfileTo.put("radius", "1500");
    Response<String> updated = template.exchange(gateUrl + "user-profile", HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(),
            hasItems("overtime: must be between 15 and 1440",
                    "radius: must be between 5 and 100",
                    "clockIn: must be between 0 and 1440",
                    "clockOut: must be between 0 and 1440"));
  }

  @Test
  public void testUpdateWithInvalidLocation() {
    String userId = createUserProfile();
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("location", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidClockIn() {
    String userId = createUserProfile();
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("clock_in", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidClockOut() {
    String userId = createUserProfile();
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("clock_out", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidOvertime() {
    String userId = createUserProfile();
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("overtime", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidRadius() {
    String userId = createUserProfile();
    Map<String, String> userProfileTo = new HashMap<>();
    userProfileTo.put("radius", "testing");
    Response<String> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidClockTime() {
    String userId = createUserProfile();
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setClockIn((short) 10);
    userProfileTo.setClockOut((short) 9);
    userProfileTo.setLocation(new GeoJsonPoint(0, 0));
    userProfileTo.setOvertime((short) 15);
    userProfileTo.setRadius((short) 5);
    Response<String> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.PUT,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_CLOCK_TIME));
    assertThat(created.getError().getMessage(), is("Invalid clock in/out time. Clock in must be less than clock out."));
  }

  @Test
  public void testRead() {
    String userId = UUID.randomUUID().toString();
    UserProfileTo userProfileTo = new UserProfileTo();
    userProfileTo.setClockIn((short) 0);
    userProfileTo.setClockOut((short) 1);
    userProfileTo.setLocation(new GeoJsonPoint(0, 0));
    userProfileTo.setOvertime((short) 15);
    userProfileTo.setRadius((short) 5);
    Response<UserProfileTo> created = template.exchange(gateUrl + "user-profile",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getError(), nullValue());
    Response<UserProfileTo> read = template.exchange(gateUrl + "user-profile", HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getError(), nullValue());
    assertThat(read.getEntity().getClockIn(), is(userProfileTo.getClockIn()));
    assertThat(read.getEntity().getClockOut(), is(userProfileTo.getClockOut()));
    assertThat(read.getEntity().getLocation().getX(), is(userProfileTo.getLocation().getX()));
    assertThat(read.getEntity().getLocation().getY(), is(userProfileTo.getLocation().getY()));
    assertThat(read.getEntity().getOvertime(), is(userProfileTo.getOvertime()));
    assertThat(read.getEntity().getRadius(), is(userProfileTo.getRadius()));
  }

  @Test
  public void testReadWithoutProfile() {
    Response<UserProfileTo> read = template.exchange(gateUrl + "user-profile", HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(UUID.randomUUID().toString()))),
            new ParameterizedTypeReference<Response<UserProfileTo>>() {
            }).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getError(), nullValue());
    assertThat(read.getEntity().getClockIn(), is((short) 540));
    assertThat(read.getEntity().getClockOut(), is((short) 1020));
    assertThat(read.getEntity().getLocation(), nullValue());
    assertThat(read.getEntity().getOvertime(), is((short) 15));
    assertThat(read.getEntity().getRadius(), is((short) 5));
  }

}
