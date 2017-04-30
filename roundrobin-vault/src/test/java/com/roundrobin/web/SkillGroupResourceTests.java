package com.roundrobin.web;

import com.roundrobin.core.api.Response;
import com.roundrobin.test.api.UnauthorizedError;
import com.roundrobin.vault.api.SkillGroupTo;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.Optional;

import static com.roundrobin.core.common.ErrorCodes.INVALID_FIELD;
import static com.roundrobin.core.common.ErrorTypes.INVALID_REQUEST_ERROR;
import static com.roundrobin.vault.common.ErrorCodes.INVALID_SKILL_GROUP_ID;
import static com.roundrobin.vault.common.ErrorCodes.SKILL_GROUP_ALREADY_EXISTS;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class SkillGroupResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.POST,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
  }

  @Test
  public void testCreateWithDuplicateName() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.POST,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
    Response<String> duplicate = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.POST,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }).getBody();
    assertThat(duplicate.getEntity(), nullValue());
    assertThat(duplicate.getError(), notNullValue());
    assertThat(duplicate.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(duplicate.getError().getCode(), is(SKILL_GROUP_ALREADY_EXISTS));
    assertThat(duplicate.getError().getMessage(), is("Skill group is same name is already exists"));
    assertThat(duplicate.getError().getParam(), is("skill_group_name"));
  }

  @Test
  public void testCreateWithEmptyValue() {
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    Response<SkillGroupTo> created = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.POST,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("groupName: may not be empty"));
  }


  @Test
  public void testCreateWithInvalidLengthName() {
    String groupName = "aadhkasjdhkajshdjashdkjashdkjahskjdhakjshdjashdajshdkajshdk";
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.POST,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getError(), notNullValue());
    assertThat(created.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(created.getError().getCode(), is(INVALID_FIELD));
    assertThat(created.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(created.getError().getFieldErrors(), hasItems("groupName: length must be between 0 and 25"));
  }

  @Test
  public void testUpdate() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created =
            template.exchange(vaultUrl + "admin/skill-group", HttpMethod.POST,
                    createHttpEntity(skillGroupTo, createAdminHeader()),
                    new ParameterizedTypeReference<Response<SkillGroupTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    skillGroupTo.setGroupName(Optional.of("NewName"));
    skillGroupTo.setId(created.getEntity().getId());
    Response<SkillGroupTo> updated = template.exchange(vaultUrl + "admin/skill-group",
            HttpMethod.PUT,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getEntity().getGroupName().get(), is("NewName"));
  }

  @Test
  public void testUpdateWithNullId() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.PUT, createHttpEntity
                    (skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(), hasItems("id: may not be empty"));
  }

  @Test
  public void testUpdateWithEmptyId() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setId(" ");
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.PUT, createHttpEntity
                    (skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(), hasItems("id: may not be empty"));
  }

  @Test
  public void testUpdateWithInvalidId() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setId("testing123");
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.PUT, createHttpEntity
                    (skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_SKILL_GROUP_ID));
    assertThat(updated.getError().getMessage(), is("Invalid skill group id"));
    assertThat(updated.getError().getParam(), is("skill_group_id"));
  }

  @Test
  public void testUpdateWithInvalidLengthName() {
    String groupName = "aadhkasjdhkajshdjashdkjashdkjahskjdhakjshdjashdajshdkajshdk";
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.PUT,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getError(), notNullValue());
    assertThat(updated.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(updated.getError().getCode(), is(INVALID_FIELD));
    assertThat(updated.getError().getMessage(), is("Invalid values specified for fields"));
    assertThat(updated.getError().getFieldErrors(), hasItems("groupName: length must be between 0 and 25"));
  }

  @Test
  public void testRead() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.POST,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
    Response<SkillGroupTo> read = template.exchange(vaultUrl + "admin/skill-group/{skillGroupId}", HttpMethod.GET,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getGroupName().get(), is(groupName));
  }

  @Test
  public void testReadWithEmptyId() {
    Response<List<SkillGroupTo>> read = template.exchange(vaultUrl + "admin/skill-group/{skillGroupId}", HttpMethod
                    .GET, createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<List<SkillGroupTo>>>() {
            }, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_GROUP_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill group id"));
    assertThat(read.getError().getParam(), is("skill_group_id"));
  }

  @Test
  public void testReadWithInvalidId() {
    Response<String> read = template.exchange(vaultUrl + "admin/skill-group/{skillGroupId}", HttpMethod.GET,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<String>>() {
            }, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_GROUP_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill group id"));
    assertThat(read.getError().getParam(), is("skill_group_id"));
  }

  @Test
  public void testDelete() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created =
            template.exchange(vaultUrl + "admin/skill-group", HttpMethod.POST,
                    createHttpEntity(skillGroupTo, createAdminHeader()),
                    new ParameterizedTypeReference<Response<SkillGroupTo>>() {
                    }).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
    Response<Boolean> deleted =
            template.exchange(vaultUrl + "admin/skill-group/{skillGroupId}", HttpMethod.DELETE,
                    createHttpEntity(createAdminHeader()),
                    new ParameterizedTypeReference<Response<Boolean>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read =
            template.exchange(vaultUrl + "admin/skill-group/{skillGroupId}", HttpMethod.GET,
                    createHttpEntity(createAdminHeader()),
                    new ParameterizedTypeReference<Response<Boolean>>() {
                    }, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_GROUP_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill group id"));
    assertThat(read.getError().getParam(), is("skill_group_id"));
  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<Boolean> read = template.exchange(vaultUrl + "admin/skill-group/{skillGroupId}", HttpMethod.DELETE,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_GROUP_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill group id"));
    assertThat(read.getError().getParam(), is("skill_group_id"));
  }

  @Test
  public void testDeleteWithInvalidId() {
    Response<Boolean> read = template.exchange(vaultUrl + "admin/skill-group/{skillGroupId}", HttpMethod.DELETE,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<Boolean>>() {
            }, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getError(), notNullValue());
    assertThat(read.getError().getType(), is(INVALID_REQUEST_ERROR));
    assertThat(read.getError().getCode(), is(INVALID_SKILL_GROUP_ID));
    assertThat(read.getError().getMessage(), is("Invalid skill group id"));
    assertThat(read.getError().getParam(), is("skill_group_id"));
  }

  @Test
  public void testList() {
    Response<List<SkillGroupTo>> list = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.GET,
            createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<List<SkillGroupTo>>>() {
            }).getBody();
    assertThat(list.getEntity(), notNullValue());
    int size = list.getEntity().size();
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.POST,
            createHttpEntity(skillGroupTo, createAdminHeader()),
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {
            }).getBody();
    assertThat(created.getEntity(), notNullValue());
    list = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.GET, createHttpEntity(createAdminHeader()),
            new ParameterizedTypeReference<Response<List<SkillGroupTo>>>() {
            }).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(size + 1));
  }

  @Test
  public void testListWithToken() {
    UnauthorizedError list = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.GET,
            createHttpEntity(createBearerHeader(getMockAccessToken())),
            UnauthorizedError.class).getBody();
    assertThat(list.getError(), is("access_denied"));
    assertThat(list.getDescription(), is("Access is denied"));
  }

  @Test
  public void testListWithInvalidAuth() {
    UnauthorizedError list = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.GET,
            createHttpEntity(createAuthHeader("abc", "cde")),
            UnauthorizedError.class).getBody();
    assertThat(list.getError(), is("Unauthorized"));
  }

  @Test
  public void testListWithoutAuth() {
    UnauthorizedError list = template.exchange(vaultUrl + "admin/skill-group", HttpMethod.GET,
            createHttpEntity(),
            UnauthorizedError.class).getBody();
    assertThat(list.getError(), is("unauthorized"));
    assertThat(list.getDescription(), is("Full authentication is required to access this resource"));
  }
}
