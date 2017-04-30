package com.roundrobin.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.vault.api.SkillDetailTo;
import com.roundrobin.vault.api.SkillTo;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Optional;

import static com.roundrobin.core.common.ErrorCodes.INVALID_FIELD;
import static com.roundrobin.core.common.ErrorTypes.INVALID_REQUEST_ERROR;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_SKILL_DETAIL_ID;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_SKILL_ID;
import static com.roundrobin.vault.common.ErrorCodes.SKILL_ALREADY_EXISTS;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

/**
 * Created by rengasu on 5/13/16.
 */
public class SkillResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    String userId = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    Response<SkillTo> read = template.exchange(vaultUrl + "skill/{skillId}", GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getMaxCost().get(), is(skillTo.getMaxCost().get()));
    assertThat(read.getEntity().getMinCost().get(), is(skillTo.getMinCost().get()));
    assertThat(read.getEntity().getTimeToComplete(), is(skillTo.getTimeToComplete()));
  }

  @Test
  public void testCreateWithInvalidSkillDetailId() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    skillTo.setTimeToComplete(Optional.of(10));
    Response<String> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_SKILL_DETAIL_ID));
    assertThat(created.getError().getMessage(), is("Invalid skill detail id"));
    assertThat(created.getError().getParam(), is("skill_detail_id"));
  }

  @Test
  public void testCreateWithInvalidTTC() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(-5));
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("timeToComplete: must be between 10 and 600"));
  }

  @Test
  public void testCreateWithEmptyValues() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(),
            hasItems("skillDetailId: may not be empty", "timeToComplete: may not be null"));
  }

  @Test
  public void testCreateWithEmptyCost() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testCreateWithNegativeCost() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setCost(Optional.of(-10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testCreateWithNoMinCost() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testCreateWithNoMaxCost() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testCreateWithNegativeMinCost() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(-5.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testCreateWithNegativeMaxCost() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(5.0));
    skillTo.setMaxCost(Optional.of(-10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testCreateWithInvalidMinMaxCost() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(15.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testCreateWithBothCostAndRange() {
    String userId = createUserProfile();
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setCost(Optional.of(5.0));
    skillTo.setMinCost(Optional.of(15.0));
    skillTo.setMaxCost(Optional.of(10.0));
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testDuplicateCreate() {
    String userId = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    Response<SkillTo> read = template.exchange(vaultUrl + "skill/{skillId}", GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    Response<SkillTo> duplicate = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(duplicate.getEntity(), nullValue());
    assertThat(duplicate.getError(), notNullValue());
    assertThat(duplicate.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(duplicate.getError().getCode(), is(SKILL_ALREADY_EXISTS));
    assertThat(duplicate.getError().getMessage(), is("Skill already exists"));
    assertThat(duplicate.getError().getParam(), is("skill_detail_id"));
  }

  @Test
  public void testUpdate() {
    String userId = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    Response<SkillTo> read = template.exchange(vaultUrl + "skill/{skillId}", GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    read.getEntity().setTimeToComplete(Optional.of(50));
    read.getEntity().setMinCost(Optional.of(50.0));
    read.getEntity().setMaxCost(Optional.of(100.0));
    Response<SkillTo> updated = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(read.getEntity(), createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), notNullValue());
    read = template.exchange(vaultUrl + "skill/{skillId}", GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity().getMaxCost().get(), is(100.0));
    assertThat(read.getEntity().getMinCost().get(), is(50.0));
    assertThat(read.getEntity().getTimeToComplete().get(), is(50));
  }

  @Test
  public void testUpdateWithEmptyValues() {
    Response<String> updated = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(new SkillTo(), createBearerHeader(getMockAccessToken(createUserProfile()))),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(), hasItems("Invalid cost value",
            "id: may not be empty"));
  }

  @Test
  public void testUpdateWithInvalidSkillId() {
    SkillTo skillTo = new SkillTo();
    skillTo.setId("testing");
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setTimeToComplete(Optional.of(10));
    Response<String> updated = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(createUserProfile())))
            , new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_SKILL_ID));
    assertThat(updated.getError().getMessage(), is("Invalid skill id"));
    assertThat(updated.getError().getParam(), is("skill_id"));
  }

  @Test
  public void testUpdateWithInvalidTTC() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(5));
    Response<String> updated = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(createUserProfile()))), new
                    ParameterizedTypeReference<Response<String>>() {
                    })
            .getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(), hasItems("timeToComplete: must be between 10 and 600"));
  }

  @Test
  public void testUpdateWithEmptyCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(createUserProfile()))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            })
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testUpdateWithNegativeCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setCost(Optional.of(-10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(createUserProfile()))), new
                    ParameterizedTypeReference<Response<SkillTo>>() {
                    })
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testUpdateWithNoMinCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(createUserProfile()))), new
                    ParameterizedTypeReference<Response<SkillTo>>() {
                    })
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testUpdateWithNoMaxCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(createUserProfile()))), new
                    ParameterizedTypeReference<Response<SkillTo>>() {
                    })
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testUpdateWithNegativeMinCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(-5.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(createUserProfile()))), new
                    ParameterizedTypeReference<Response<SkillTo>>() {
                    })
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testUpdateWithNegativeMaxCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(5.0));
    skillTo.setMaxCost(Optional.of(-10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(createUserProfile()))), new
                    ParameterizedTypeReference<Response<SkillTo>>() {
                    })
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testUpdateWithInvalidMinMaxCost() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setMinCost(Optional.of(15.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId("testing");
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", PUT,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(createUserProfile()))), new
                    ParameterizedTypeReference<Response<SkillTo>>() {
                    })
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testUpdateWithBothCostAndRange() {
    SkillTo skillTo = new SkillTo();
    skillTo.setTimeToComplete(Optional.of(10));
    skillTo.setCost(Optional.of(5.0));
    skillTo.setMinCost(Optional.of(15.0));
    skillTo.setMaxCost(Optional.of(10.0));
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", PUT, createHttpEntity(skillTo,
            createBearerHeader(getMockAccessToken(createUserProfile()))), new
            ParameterizedTypeReference<Response<SkillTo>>() {
            })
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("Invalid cost value"));
  }

  @Test
  public void testReadWithEmptyId() {
    Response<List<SkillTo>> read =
            template.exchange(vaultUrl + "skill/{skillId}", GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(createUserProfile()))),
                    new ParameterizedTypeReference<Response<List<SkillTo>>>() {
                    }, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill id"));
    assertThat(read.getError().getParam(), is("skill_id"));
  }

  @Test
  public void testReadWithInvalidId() {
    Response<SkillTo> read = template.exchange(vaultUrl + "skill/{skillId}", GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(createUserProfile()))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill id"));
    assertThat(read.getError().getParam(), is("skill_id"));
  }

  @Test
  public void testDelete() {
    String userId = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    Response<SkillTo> read = template.exchange(vaultUrl + "skill/{skillId}", GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    Response<Boolean> deleted =
            template.exchange(vaultUrl + "skill/{skillId}", DELETE,
                    createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
                    new ParameterizedTypeReference<Response<Boolean>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    read = template.exchange(vaultUrl + "skill/{skillId}", GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill id"));
    assertThat(read.getError().getParam(), is("skill_id"));
  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<SkillTo> read = template.exchange(vaultUrl + "skill/{skillId}", DELETE,
            createHttpEntity(createBearerHeader(getMockAccessToken(createUserProfile()))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill id"));
    assertThat(read.getError().getParam(), is("skill_id"));
  }

  @Test
  public void testDeleteWithInvalidId() {
    Response<SkillTo> read =
            template.exchange(vaultUrl + "skill/{skillId}", DELETE,
                    createHttpEntity(createBearerHeader(getMockAccessToken(createUserProfile()))),
                    new ParameterizedTypeReference<Response<SkillTo>>() {
                    }, "testing1").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill id"));
    assertThat(read.getError().getParam(), is("skill_id"));
  }

  @Test
  public void testList() {
    String userId = createUserProfile();
    SkillDetailTo skillDetailTo = createSkillDetail();
    SkillTo skillTo = new SkillTo();
    skillTo.setMinCost(Optional.of(1.0));
    skillTo.setMaxCost(Optional.of(10.0));
    skillTo.setSkillDetailId(skillDetailTo.getId());
    skillTo.setTimeToComplete(Optional.of(10));
    Response<SkillTo> created = template.exchange(vaultUrl + "skill", POST,
            createHttpEntity(skillTo, createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<SkillTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    Response<List<SkillTo>> list = template.exchange(vaultUrl + "skill", GET,
            createHttpEntity(createBearerHeader(getMockAccessToken(userId))),
            new ParameterizedTypeReference<Response<List<SkillTo>>>() {
            }).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(1));
  }

  @Test
  public void testListWithEmptyId() {
    Response<List<SkillTo>> list =
            template.exchange(vaultUrl + "skill", GET,
                    createHttpEntity(createBearerHeader(getMockAccessToken(createUserProfile()))),
                    new ParameterizedTypeReference<Response<List<SkillTo>>>() {
                    }).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getError(), nullValue());
    assertThat(list.getEntity().size(), is(0));
  }
}
