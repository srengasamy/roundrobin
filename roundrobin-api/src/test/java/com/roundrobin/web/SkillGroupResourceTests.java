package com.roundrobin.web;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;
import java.util.StringJoiner;

import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.api.SkillGroupTo;
import com.roundrobin.common.ErrorCode;

public class SkillGroupResourceTests extends ResourceTests {

  @Test
  public void testCreate() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
  }

  @Test
  public void testCreateWithDuplicateName() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
    Response<String> duplicate = helper
            .post(url + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<String>>() {
            }, port).getBody();
    assertThat(duplicate.getEntity(), nullValue());
    assertThat(duplicate.getErrors(), notNullValue());
    assertThat(duplicate.getErrors(), hasItems(new Error(ErrorCode.SKILL_GROUP_ALREADY_EXISTS.getCode(),
            messages.getErrorMessage(ErrorCode.SKILL_GROUP_ALREADY_EXISTS))));
  }

  @Test
  public void testCreateWithNoName() {
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD.getCode(),
            "groupName: may not be empty")));
  }

  @Test
  public void testCreateWithInvalidName() {
    String groupName = "Testing:" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD.getCode(),
            "groupName: must match \"^[A-Za-z0-9]*$\"")));
  }

  @Test
  public void testCreateWithInvalidLengthName() {
    String groupName = "aadhkasjdhkajshdjashdkjashdkjahskjdhakjshdjashdajshdkajshdk";
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD.getCode(),
            "groupName: length must be between 0 and 25")));
  }

  @Test
  public void testUpdate() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), notNullValue());
    skillGroupTo.setGroupName(Optional.of("NewName"));
    skillGroupTo.setId(created.getEntity().getId());
    Response<SkillGroupTo> updated = helper.put(url + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<SkillGroupTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), notNullValue());
    assertThat(updated.getEntity().getGroupName().get(), is("NewName"));
  }

  @Test
  public void testUpdateWithNullId() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = helper.put(url + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<SkillGroupTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD.getCode(),
            "id: may not be empty")));
  }

  @Test
  public void testUpdateWithEmptyId() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setId(" ");
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = helper.put(url + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<SkillGroupTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD.getCode(),
            "id: may not be empty")));
  }

  @Test
  public void testUpdateWithInvalidId() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setId("testing123");
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = helper.put(url + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<SkillGroupTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(new Error(ErrorCode.INVALID_SKILL_GROUP_ID.getCode(),
            messages.getErrorMessage(ErrorCode.INVALID_SKILL_GROUP_ID))));
  }

  @Test
  public void testUpdateWithInvalidLengthName() {
    String groupName = "aadhkasjdhkajshdjashdkjashdkjahskjdhakjshdjashdajshdkajshdk";
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> updated = helper.put(url + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<SkillGroupTo>>() {
    }, port).getBody();
    assertThat(updated.getEntity(), nullValue());
    assertThat(updated.getErrors(), notNullValue());
    assertThat(updated.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD.getCode(),
            "groupName: length must be between 0 and 25")));
  }

  @Test
  public void testRead() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
    Response<SkillGroupTo> read = helper.get(url + "skill-group/{skillGroupId}", new ParameterizedTypeReference<Response<SkillGroupTo>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getGroupName().get(), is(groupName));
  }

  @Test
  public void testReadWithEmptyId() {
    Response<String> read = helper.get(url + "skill-group/{skillGroupId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(new Error(ErrorCode.INVALID_URL.getCode(),
            messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testReadWithInvalidId() {
    Response<String> read = helper.get(url + "skill-group/{skillGroupId}", new ParameterizedTypeReference<Response<String>>() {
    }, port, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(new Error(ErrorCode.INVALID_SKILL_GROUP_ID.getCode(),
            messages.getErrorMessage(ErrorCode.INVALID_SKILL_GROUP_ID))));
  }

  @Test
  public void testDelete() {
    String groupName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), notNullValue());
    assertThat(created.getEntity().getGroupName().get(), is(groupName));
    Response<Boolean> deleted = helper.delete(url + "skill-group/{skillGroupId}", new ParameterizedTypeReference<Response<Boolean>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read = helper.get(url + "skill-group/{skillGroupId}", new ParameterizedTypeReference<Response<Boolean>>() {
    }, port, created.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(new Error(ErrorCode.INVALID_SKILL_GROUP_ID.getCode(),
            messages.getErrorMessage(ErrorCode.INVALID_SKILL_GROUP_ID))));
  }

  @Test
  public void testDeleteWithEmptyId() {
    Response<Boolean> read = helper.delete(url + "skill-group/{skillGroupId}", new ParameterizedTypeReference<Response<Boolean>>() {
    }, port, " ").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(new Error(ErrorCode.INVALID_URL.getCode(),
            messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testDeleteWithInvalidId() {
    Response<Boolean> read = helper.delete(url + "skill-group/{skillGroupId}", new ParameterizedTypeReference<Response<Boolean>>() {
    }, port, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), notNullValue());
    assertThat(read.getErrors(), hasItems(new Error(ErrorCode.INVALID_SKILL_GROUP_ID.getCode(),
            messages.getErrorMessage(ErrorCode.INVALID_SKILL_GROUP_ID))));
  }
}
