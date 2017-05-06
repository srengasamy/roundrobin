package com.roundrobin.gate.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.gate.ResourceTests;
import com.roundrobin.gate.api.SkillDetailTo;
import com.roundrobin.gate.api.SkillGroupTo;
import com.roundrobin.gate.enums.DeliveryType;
import com.roundrobin.test.api.UnauthorizedError;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.roundrobin.core.common.ErrorCodes.INVALID_FIELD;
import static com.roundrobin.core.common.ErrorCodes.UNPARSABLE_INPUT;
import static com.roundrobin.core.common.ErrorTypes.INVALID_REQUEST_ERROR;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_SKILL_DETAIL_ID;
import static com.roundrobin.gate.common.ErrorCodes.INVALID_SKILL_GROUP_ID;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by rengasu on 5/10/16.
 */
public class SkillDetailResourceTests extends ResourceTests {
  @Test
  public void testCreate() {
    String groupName = "Testing" + System.currentTimeMillis();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> createdGroup = createSkillGroup(skillGroupTo);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    assertThat(createdSkill.getEntity().getName().get(), is(skillName));
    assertThat(createdSkill.getEntity().getId(), notNullValue());
    assertThat(createdSkill.getEntity().getDeliveryType().get(), is(DeliveryType.HOME));
  }

  @Test
  public void testCreateWithNullValues() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    Response<String> created = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(),
            hasItems("name: may not be empty", "skillGroupId: may not be empty", "deliveryType: may not be null"));
  }

  @Test
  public void testCreateWithInvalidDeliveryType() {
    Map<String, String> skillDetailTo = new HashMap<>();
    skillDetailTo.put("delivery_type", "a");
    Response<String> created = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testCreateWithEmptyValues() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setSkillGroupId(" ");
    skillDetailTo.setName(Optional.of(" "));
    Response<String> created = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(),
            hasItems("name: may not be empty", "skillGroupId: may not be empty", "deliveryType: may not be null"));
  }

  @Test
  public void testCreateWithInvalidValues() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setName(Optional.of(":"));
    Response<String> created = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("skillGroupId: may not be empty",
            "deliveryType: may not be null"));
  }

  @Test
  public void testCreateWithInvalidNameLength() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setName(Optional.of("sadasldasldhajshdkasjhdkashdjkashdkjashkjdhasjkdhkjashdkjashdkjashkjdhaskdhak"));
    Response<String> created = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("name: length must be between 0 and 25",
            "skillGroupId: may not be empty", "deliveryType: may not be null"));
  }

  @Test
  public void testCreateWithInvalidGroupId() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setName(Optional.of("testing"));
    skillDetailTo.setDeliveryType(Optional.of(DeliveryType.HOME));
    skillDetailTo.setSkillGroupId("testing");
    Response<String> created = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_SKILL_GROUP_ID));
    assertThat(created.getError().getMessage(), is("Invalid skill group id"));
    assertThat(created.getError().getParam(), is("skill_group_id"));
  }

  @Test
  public void testUpdate() {
    String groupName = "Testing" + System.currentTimeMillis();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> createdGroup = createSkillGroup(skillGroupTo);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    createdSkill.getEntity().setName(Optional.of("newname"));
    Response<SkillDetailTo> updatedSkill = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.PUT,
            createHttpEntity(createdSkill.getEntity(), createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    assertThat(updatedSkill.getEntity(), notNullValue());
    Response<SkillDetailTo> read =
            template.exchange(gateUrl + "admin/skill-detail/{skillDetailId}", HttpMethod.GET,
                    createHttpEntity(createAdminHeader()),
                    new ParameterizedTypeReference<Response<SkillDetailTo>>() {
                    }, updatedSkill.getEntity().getId())
                    .getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getName().get(), is("newname"));
  }

  @Test
  public void testUpdateWithEmptySkillId() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    Response<SkillDetailTo> updatedSkill = template.exchange(gateUrl + "admin/skill-detail",
            HttpMethod.PUT,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    assertThat(updatedSkill.getEntity(), nullValue());
    assertThat(updatedSkill.getError(), notNullValue());
    assertThat(updatedSkill.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updatedSkill.getError().getCode(), is(INVALID_FIELD));
    assertThat(updatedSkill.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updatedSkill.getError().getFieldErrors(), hasItems("id: may not be empty"));
  }

  @Test
  public void testUpdateWithInvalidSkillId() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setId("testing");
    Response<SkillDetailTo> updatedSkill = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.PUT,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    assertThat(updatedSkill.getEntity(), nullValue());
    assertThat(updatedSkill.getError(), notNullValue());
    assertThat(updatedSkill.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updatedSkill.getError().getCode(), is(INVALID_SKILL_DETAIL_ID));
    assertThat(updatedSkill.getError().getMessage(), is("Invalid skill detail id"));
    assertThat(updatedSkill.getError().getParam(), is("skill_detail_id"));
  }

  @Test
  public void testUpdateWithInvalidNameLength() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setName(Optional.of("asdasdjaskhdkasajsdhajkshdjkashdjkashdjkhasjkdhkajshdkjashdkjash"));
    Response<SkillDetailTo> updatedSkill = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.PUT,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    assertThat(updatedSkill.getEntity(), nullValue());
    assertThat(updatedSkill.getError(), notNullValue());
    assertThat(updatedSkill.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updatedSkill.getError().getCode(), is(INVALID_FIELD));
    assertThat(updatedSkill.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updatedSkill.getError().getFieldErrors(), hasItems("name: length must be between 0 and 25"));
  }

  @Test
  public void testUpdateWithInvalidDeliveryType() {
    Map<String, String> skillDetailTo = new HashMap<>();
    skillDetailTo.put("delivery_type", "a");
    Response<String> created = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.PUT,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(UNPARSABLE_INPUT));
    assertThat(created.getError().getMessage(), is("Unable to parse input"));
  }

  @Test
  public void testRead() {
    String groupName = "Testing" + System.currentTimeMillis();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> createdGroup = createSkillGroup(skillGroupTo);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    Response<SkillDetailTo> read =
            template.exchange(gateUrl + "admin/skill-detail/{skillDetailId}", HttpMethod.GET,
                    createHttpEntity(createAdminHeader()),
                    new ParameterizedTypeReference<Response<SkillDetailTo>>() {
                    }, createdSkill.getEntity().getId())
                    .getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getName().get(), is(skillName));
    assertThat(read.getEntity().getDeliveryType().get(), is(DeliveryType.HOME));
  }

  @Test
  public void testReadEmptyId() {
    Response<List<SkillDetailTo>> read = template.exchange(gateUrl + "admin/skill-detail/{skillDetailId}",
            HttpMethod.GET,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<List<SkillDetailTo>>>() {
            }, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_DETAIL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill detail id"));
    assertThat(read.getError().getParam(), is("skill_detail_id"));
  }

  @Test
  public void testReadEmptyWithInvalidId() {
    Response<SkillDetailTo> read = template.exchange(gateUrl + "admin/skill-detail/{skillDetailId}",
            HttpMethod.GET,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_DETAIL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill detail id"));
    assertThat(read.getError().getParam(), is("skill_detail_id"));
  }

  @Test
  public void testDelete() {
    String groupName = "Testing" + System.currentTimeMillis();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> createdGroup = createSkillGroup(skillGroupTo);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    Response<Boolean> deleted = template.exchange(gateUrl + "admin/skill-detail/{skillDetailId}", HttpMethod.DELETE,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, createdSkill.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read = template.exchange(gateUrl + "admin/skill-detail/{skillDetailId}", HttpMethod.GET,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, createdSkill.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_DETAIL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill detail id"));
    assertThat(read.getError().getParam(), is("skill_detail_id"));
  }

  @Test
  public void testDeleteEmptyId() {
    Response<String> read = template.exchange(gateUrl + "admin/skill-detail/{skillDetailId}", HttpMethod.DELETE,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_DETAIL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill detail id"));
    assertThat(read.getError().getParam(), is("skill_detail_id"));
  }

  @Test
  public void testDeleteEmptyWithInvalidId() {
    Response<String> read = template.exchange(gateUrl + "admin/skill-detail/{skillDetailId}", HttpMethod.DELETE,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_DETAIL_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill detail id"));
    assertThat(read.getError().getParam(), is("skill_detail_id"));
  }

  @Test
  public void testList() {
    Response<List<SkillDetailTo>> list = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.GET,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<List<SkillDetailTo>>>() {
            }).getBody();
    assertThat(list.getEntity(), notNullValue());
    int size = list.getEntity().size();
    String groupName = "Testing" + System.currentTimeMillis();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> createdGroup = createSkillGroup(skillGroupTo);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.POST,
            createHttpEntity(skillDetailTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillDetailTo>>() {
            }).getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    list = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.GET,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<List<SkillDetailTo>>>() {
            }).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(size + 1));
  }

  @Test
  public void testListWithToken() {
    UnauthorizedError list = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken())),
            UnauthorizedError.class).getBody();
    assertThat(list.getError(), is("access_denied"));
    assertThat(list.getDescription(), is("Access is denied"));
  }

  @Test
  public void testListWithInvalidAuth() {
    UnauthorizedError list = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.GET,
            createHttpEntity(createAuthHeader("abc", "cde")),
            UnauthorizedError.class).getBody();
    assertThat(list.getError(), is("Unauthorized"));
  }

  @Test
  public void testListWithoutAuth() {
    UnauthorizedError list = template.exchange(gateUrl + "admin/skill-detail", HttpMethod.GET,
            createHttpEntity(),
            UnauthorizedError.class).getBody();
    assertThat(list.getError(), is("unauthorized"));
    assertThat(list.getDescription(), is("Full authentication is required to access this resource"));
  }
}
