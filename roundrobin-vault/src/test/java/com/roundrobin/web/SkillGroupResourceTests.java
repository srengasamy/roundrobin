package com.roundrobin.web;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.vault.api.SkillGroupTo;
import com.roundrobin.vault.common.ErrorCode;

public class SkillGroupResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = helper.post(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
  }

  @Test
  public void testCreateWithDuplicateName() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = helper.post(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
    Response<String> duplicate = helper.post(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<String>>() {}).getBody();
    assertThat(duplicate.getEntity(), nullValue());
    assertThat(duplicate.getErrors(), notNullValue());
    assertThat(duplicate.getErrors(), hasItems(new Error(ErrorCode.SKILL_GROUP_ALREADY_EXISTS,
        messages.getErrorMessage(ErrorCode.SKILL_GROUP_ALREADY_EXISTS))));
  }

  @Test
  public void testCreateWithNoName() {
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    Response<SkillGroupTo> created = helper.post(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "groupName: may not be empty")));
  }

  @Test
  public void testCreateWithInvalidName() {
    String groupName = "Testing:" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = helper.post(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "groupName: must match \"^[A-Za-z0-9]*$\"")));
  }

  @Test
  public void testCreateWithInvalidLengthName() {
    String groupName = "aadhkasjdhkajshdjashdkjashdkjahskjdhakjshdjashdajshdkajshdk";
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = helper.post(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "groupName: length must be between 0 and 25")));
  }

  @Test
  public void testUpdate() {
    String username = createUser();
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created =
        helper.post(vaultUrl + "skill-group", createBearerHeaders(getAccessToken(username)), skillGroupTo,
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    skillGroupTo.setGroupName(Optional.of("NewName"));
    skillGroupTo.setId(created.getEntity().getId());
    Response<SkillGroupTo> updated = helper.put(vaultUrl + "skill-group", createBearerHeaders(getAccessToken(username)),
        skillGroupTo, new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getEntity().getGroupName().get(), is("NewName"));
  }

  @Test
  public void testUpdateWithNullId() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = helper.put(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "id: may not be empty")));
  }

  @Test
  public void testUpdateWithEmptyId() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setId(" ");
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = helper.put(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "id: may not be empty")));
  }

  @Test
  public void testUpdateWithInvalidId() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setId("testing123");
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = helper.put(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_GROUP_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_GROUP_ID))));
  }

  @Test
  public void testUpdateWithInvalidLengthName() {
    String groupName = "aadhkasjdhkajshdjashdkjashdkjahskjdhakjshdjashdajshdkajshdk";
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = helper.put(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "groupName: length must be between 0 and 25")));
  }

  @Test
  public void testRead() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = helper.post(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
    Response<SkillGroupTo> read = helper.get(vaultUrl + "skill-group/{skillGroupId}", createBearerHeaders(),
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getGroupName().get(), is(groupName));
  }

  @Test
  public void testReadWithEmptyId() {
    Response<List<SkillGroupTo>> read = helper.get(vaultUrl + "skill-group/{skillGroupId}", createBearerHeaders(),
        new ParameterizedTypeReference<Response<List<SkillGroupTo>>>() {}, " ").getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().size(), not(is(0)));
  }

  @Test
  public void testReadWithInvalidId() {
    Response<String> read = helper.get(vaultUrl + "skill-group/{skillGroupId}", createBearerHeaders(),
        new ParameterizedTypeReference<Response<String>>() {}, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_GROUP_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_GROUP_ID))));
  }

  @Test
  public void testDelete() {
    String username = createUser();
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created =
        helper.post(vaultUrl + "skill-group", createBearerHeaders(getAccessToken(username)), skillGroupTo,
            new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
    Response<Boolean> deleted =
        helper.delete(vaultUrl + "skill-group/{skillGroupId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<Boolean>>() {}, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read =
        helper.get(vaultUrl + "skill-group/{skillGroupId}", createBearerHeaders(getAccessToken(username)),
            new ParameterizedTypeReference<Response<Boolean>>() {}, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_GROUP_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_GROUP_ID))));
  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<Boolean> read = helper.delete(vaultUrl + "skill-group/{skillGroupId}", createBearerHeaders(),
        new ParameterizedTypeReference<Response<Boolean>>() {}, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_URL, messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testDeleteWithInvalidId() {
    Response<Boolean> read = helper.delete(vaultUrl + "skill-group/{skillGroupId}", createBearerHeaders(),
        new ParameterizedTypeReference<Response<Boolean>>() {}, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_GROUP_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_GROUP_ID))));
  }

  @Test
  public void testList() {
    Response<List<SkillGroupTo>> list = helper.get(vaultUrl + "skill-group", createBearerHeaders(),
        new ParameterizedTypeReference<Response<List<SkillGroupTo>>>() {}).getBody();
    assertThat(list.getEntity(), notNullValue());
    int size = list.getEntity().size();
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = helper.post(vaultUrl + "skill-group", createBearerHeaders(), skillGroupTo,
        new ParameterizedTypeReference<Response<SkillGroupTo>>() {}).getBody();
    assertThat(created.getEntity(), notNullValue());
    list = helper.get(vaultUrl + "skill-group", createBearerHeaders(),
        new ParameterizedTypeReference<Response<List<SkillGroupTo>>>() {}).getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(size + 1));
  }

}
