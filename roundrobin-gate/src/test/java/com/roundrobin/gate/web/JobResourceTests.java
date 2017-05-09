package com.roundrobin.gate.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.gate.ResourceTests;
import com.roundrobin.gate.api.JobTo;
import com.roundrobin.gate.api.UserProfileTo;
import com.roundrobin.gate.enums.JobStatus;
import com.roundrobin.test.api.UnauthorizedError;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.roundrobin.core.common.ErrorCodes.INVALID_FIELD;
import static com.roundrobin.core.common.ErrorCodes.UNPARSABLE_INPUT;
import static com.roundrobin.core.common.ErrorTypes.INVALID_REQUEST_ERROR;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_JOB_ID;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_PREF_DATE;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_SKILL_DETAIL_ID;
import static com.roundrobin.gate.enums.VendorPreference.TOP_RATED;
import static com.roundrobin.gate.enums.VendorPreference.URGENT;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by rengasu on 5/8/17.
 */
public class JobResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    JobTo jobTo = new JobTo();
    jobTo.setPreferredStart(DateTime.now());
    jobTo.setPreferredEnd(DateTime.now().plusDays(1));
    jobTo.setDesc("Sample desc");
    jobTo.setSkillDetailId(createSkillDetail().getId());
    jobTo.setLocation(new GeoJsonPoint(0, 0));
    jobTo.setVendorPref(URGENT);
    Response<JobTo> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getError(), nullValue());
    assertThat(created.getEntity().getId(), notNullValue());
    assertThat(created.getEntity().getCreated(), notNullValue());
    assertThat(created.getEntity().getDesc(), is(jobTo.getDesc()));
    assertThat(created.getEntity().getLocation(), is(jobTo.getLocation()));
    assertThat(created.getEntity().getPreferredStart().getMillis(), is(jobTo.getPreferredStart().getMillis()));
    assertThat(created.getEntity().getPreferredEnd().getMillis(), is(jobTo.getPreferredEnd().getMillis()));
    assertThat(created.getEntity().getVendorPref(), is(jobTo.getVendorPref()));
    assertThat(created.getEntity().getSkillDetailId(), is(jobTo.getSkillDetailId()));
    assertThat(created.getEntity().getStatus(), is(JobStatus.SCHEDULED));
  }

  @Test
  public void testCreateInvalidSkillDetailId() {
    JobTo jobTo = new JobTo();
    jobTo.setPreferredStart(DateTime.now());
    jobTo.setPreferredEnd(DateTime.now().plusDays(1));
    jobTo.setDesc("Sample desc");
    jobTo.setSkillDetailId("abcd");
    jobTo.setLocation(new GeoJsonPoint(0, 0));
    jobTo.setVendorPref(URGENT);
    Response<JobTo> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_SKILL_DETAIL_ID));
    assertThat(created.getError().getMessage(), is("Invalid skill detail id"));
    assertThat(created.getError().getParam(), is("skill_detail_id"));
  }

  @Test
  public void testCreateWithInvalidPrefStartDate() {
    JobTo jobTo = new JobTo();
    jobTo.setPreferredStart(DateTime.now().minusDays(1));
    jobTo.setPreferredEnd(DateTime.now());
    jobTo.setDesc("Sample desc");
    jobTo.setSkillDetailId(createSkillDetail().getId());
    jobTo.setLocation(new GeoJsonPoint(0, 0));
    jobTo.setVendorPref(URGENT);
    Response<JobTo> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_PREF_DATE));
    assertThat(created.getError().getMessage(), is("Invalid preferred date"));
    assertThat(created.getError().getParam(), is("preferred_start"));
  }

  @Test
  public void testCreateWithInvalidPrefEndDate() {
    JobTo jobTo = new JobTo();
    jobTo.setPreferredStart(DateTime.now());
    jobTo.setPreferredEnd(DateTime.now().minusDays(1));
    jobTo.setDesc("Sample desc");
    jobTo.setSkillDetailId(createSkillDetail().getId());
    jobTo.setLocation(new GeoJsonPoint(0, 0));
    jobTo.setVendorPref(URGENT);
    Response<JobTo> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_PREF_DATE));
    assertThat(created.getError().getMessage(), is("Invalid preferred date"));
    assertThat(created.getError().getParam(), is("preferred_end"));
  }


  @Test
  public void testCreateWithInvalidPrefDate() {
    JobTo jobTo = new JobTo();
    jobTo.setPreferredStart(DateTime.now().plusDays(1));
    jobTo.setPreferredEnd(DateTime.now());
    jobTo.setDesc("Sample desc");
    jobTo.setSkillDetailId(createSkillDetail().getId());
    jobTo.setLocation(new GeoJsonPoint(0, 0));
    jobTo.setVendorPref(URGENT);
    Response<JobTo> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_PREF_DATE));
    assertThat(created.getError().getMessage(), is("Invalid preferred date"));
    assertThat(created.getError().getParam(), is("preferred_date"));
  }

  @Test
  public void testCreateWithEmptyValue() {
    Response<JobTo> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(new JobTo(), createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(),
            hasItems("location: may not be null",
                    "vendorPref: may not be null",
                    "skillDetailId: may not be null"));
  }

  @Test
  public void testCreateWithInvalidLocation() {
    Map<String, String> jobTo = new HashMap<>();
    jobTo.put("location", "testing");
    Response<String> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testCreateWithInvalidVendorPref() {
    Map<String, String> jobTo = new HashMap<>();
    jobTo.put("vendor_pref", "testing");
    Response<String> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testCreateWithInvalidPrefStart() {
    Map<String, String> jobTo = new HashMap<>();
    jobTo.put("preferred_start", "testing");
    Response<String> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testCreateWithInvalidPrefEnd() {
    Map<String, String> jobTo = new HashMap<>();
    jobTo.put("preferred_end", "testing");
    Response<String> created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testCreateWithEmptyToken() {
    UnauthorizedError created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(new HashMap<String, String>()), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("unauthorized"));
  }

  @Test
  public void testCreateWithInvalidToken() {
    UnauthorizedError created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(createBearerHeader("testing")), UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
  }

  @Test
  public void testCreateWithInvalidScope() {
    String token = getMockAccessToken(UUID.randomUUID().toString(), (System.currentTimeMillis() + 120001) / 1000,
            getInvalidScopes(), getValidResources());
    UserProfileTo userProfileTo = new UserProfileTo();
    UnauthorizedError created = template.exchange(gateUrl + "job",
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
    UnauthorizedError created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(token)),
            UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("access_denied"));
    assertThat(created.getDescription(), is("Invalid token does not contain resource id (web)"));
  }

  @Test
  public void testCreateWithExpiredToken() {
    String token = getMockAccessToken(UUID.randomUUID().toString(), (System.currentTimeMillis() - 120001) / 1000,
            getValidScopes(), getValidResources());
    UserProfileTo userProfileTo = new UserProfileTo();
    UnauthorizedError created = template.exchange(gateUrl + "job",
            HttpMethod.POST,
            createHttpEntity(userProfileTo, createBearerHeader(token)),
            UnauthorizedError.class).getBody();
    assertThat(created.getError(), is("invalid_token"));
    assertThat(created.getDescription(), is("Access token expired: " + token));
  }

  @Test
  public void testUpdate() {
    String userId = UUID.randomUUID().toString();
    String jobId = createJob(userId);
    JobTo jobTo = new JobTo();
    jobTo.setId(jobId);
    jobTo.setPreferredStart(DateTime.now().plusDays(10));
    jobTo.setPreferredEnd(DateTime.now().plusDays(11));
    jobTo.setDesc("Sample desc1");
    jobTo.setLocation(new GeoJsonPoint(2, 2));
    jobTo.setVendorPref(TOP_RATED);
    Response<JobTo> updated = template.exchange(gateUrl + "job",
            HttpMethod.PUT,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getError(), nullValue());
    assertThat(updated.getEntity().getDesc(), is(jobTo.getDesc()));
    assertThat(updated.getEntity().getLocation(), is(jobTo.getLocation()));
    assertThat(updated.getEntity().getPreferredStart().getMillis(), is(jobTo.getPreferredStart().getMillis()));
    assertThat(updated.getEntity().getPreferredEnd().getMillis(), is(jobTo.getPreferredEnd().getMillis()));
    assertThat(updated.getEntity().getVendorPref(), is(jobTo.getVendorPref()));
  }

  @Test
  public void testUpdateWithEmptyValue() {
    String userId = UUID.randomUUID().toString();
    JobTo jobTo = new JobTo();
    Response<JobTo> updated = template.exchange(gateUrl + "job",
            HttpMethod.PUT,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(), hasItems("id: may not be empty"));
  }

  @Test
  public void testUpdateWithInvalidLocation() {
    Map<String, String> jobTo = new HashMap<>();
    jobTo.put("location", "testing");
    Response<JobTo> updated = template.exchange(gateUrl + "job",
            HttpMethod.PUT,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(updated.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidPrefStart() {
    Map<String, String> jobTo = new HashMap<>();
    jobTo.put("preferred_start", "testing");
    Response<JobTo> updated = template.exchange(gateUrl + "job",
            HttpMethod.PUT,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(updated.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidPrefEnd() {
    Map<String, String> jobTo = new HashMap<>();
    jobTo.put("preferred_end", "testing");
    Response<JobTo> updated = template.exchange(gateUrl + "job",
            HttpMethod.PUT,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(updated.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testUpdateWithInvalidPrefStartDate() {
    JobTo jobTo = new JobTo();
    jobTo.setPreferredStart(DateTime.now().minusDays(1));
    jobTo.setId("testing");
    Response<JobTo> updated = template.exchange(gateUrl + "job",
            HttpMethod.PUT,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_PREF_DATE));
    assertThat(updated.getError().getMessage(), is("Invalid preferred date"));
    assertThat(updated.getError().getParam(), is("preferred_start"));
  }

  @Test
  public void testUpdateWithInvalidPrefEndDate() {
    JobTo jobTo = new JobTo();
    jobTo.setPreferredEnd(DateTime.now().minusDays(1));
    jobTo.setId("testing");
    Response<JobTo> updated = template.exchange(gateUrl + "job",
            HttpMethod.PUT,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_PREF_DATE));
    assertThat(updated.getError().getMessage(), is("Invalid preferred date"));
    assertThat(updated.getError().getParam(), is("preferred_end"));
  }

  @Test
  public void testUpdateWithInvalidPrefDate() {
    JobTo jobTo = new JobTo();
    jobTo.setPreferredStart(DateTime.now().plusDays(11));
    jobTo.setPreferredEnd(DateTime.now().plusDays(10));
    jobTo.setId("testing");
    Response<JobTo> updated = template.exchange(gateUrl + "job",
            HttpMethod.PUT,
            createHttpEntity(jobTo, createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_PREF_DATE));
    assertThat(updated.getError().getMessage(), is("Invalid preferred date"));
    assertThat(updated.getError().getParam(), is("preferred_date"));
  }

  @Test
  public void testRead() {
    String userId = UUID.randomUUID().toString();
    String jobId = createJob(userId);
    Response<JobTo> read = template.exchange(gateUrl + "job/{jobId}",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }, jobId).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getError(), nullValue());
    assertThat(read.getEntity().getId(), notNullValue());
    assertThat(read.getEntity().getStatus(), is(JobStatus.SCHEDULED));
  }

  @Test
  public void testReadInvalidJobId() {
    String userId = UUID.randomUUID().toString();
    String jobId = createJob(userId);
    Response<JobTo> read = template.exchange(gateUrl + "job/{jobId}",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }, jobId).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_JOB_ID));
    assertThat(read.getError().getMessage(), is("Invalid job id"));
    assertThat(read.getError().getParam(), is("job_id"));
  }

  @Test
  public void testDelete() {
    String userId = UUID.randomUUID().toString();
    String jobId = createJob(userId);
    Response<Boolean> delete = template.exchange(gateUrl + "job/{jobId}",
            HttpMethod.DELETE,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, jobId).getBody();
    assertThat(delete.getEntity(), notNullValue());
    assertThat(delete.getError(), nullValue());
    assertThat(delete.getEntity(), is(true));
    Response<JobTo> read = template.exchange(gateUrl + "job/{jobId}",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<JobTo>>() {
            }, jobId).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_JOB_ID));
    assertThat(read.getError().getMessage(), is("Invalid job id"));
    assertThat(read.getError().getParam(), is("job_id"));
  }

  @Test
  public void testDeleteWithInvalidId() {
    String userId = UUID.randomUUID().toString();
    String jobId = createJob(userId);
    Response<Boolean> delete = template.exchange(gateUrl + "job/{jobId}",
            HttpMethod.DELETE,
            createHttpEntity(createBearerHeader(getMockAccessToken())),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, jobId).getBody();
    assertThat(delete.getEntity(), nullValue());
    assertThat(delete.getError(), notNullValue());
    assertThat(delete.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(delete.getError().getCode(), is(INVALID_JOB_ID));
    assertThat(delete.getError().getMessage(), is("Invalid job id"));
    assertThat(delete.getError().getParam(), is("job_id"));
  }

  @Test
  public void testList() {
    String userId = UUID.randomUUID().toString();
    String jobId = createJob(userId);
    Response<List<JobTo>> list = template.exchange(gateUrl + "job",
            HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<List<JobTo>>>() {
            }, jobId).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getError(), nullValue());
    Optional<JobTo> read = list.getEntity().stream().filter(j -> j.getId().equals(jobId)).findFirst();
    assertThat(read.isPresent(), is(true));
  }
}
