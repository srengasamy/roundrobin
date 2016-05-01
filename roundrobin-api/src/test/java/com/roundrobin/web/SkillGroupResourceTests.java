package com.roundrobin.web;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

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
        .post(url + "skill-group", skillGroupTo, new ParameterizedTypeReference<Response<String>>() {}, port).getBody();
    assertThat(duplicate.getEntity(), nullValue());
    assertThat(duplicate.getErrors(), notNullValue());
    assertThat(duplicate.getErrors().size(), is(1));
    assertThat(duplicate.getErrors().get(0).getCode(), is(ErrorCode.SKILL_GROUP_ALREADY_EXISTS.getCode()));
    assertThat(duplicate.getErrors().get(0).getMessage(),
        is(messages.getErrorMessage(ErrorCode.SKILL_GROUP_ALREADY_EXISTS)));
  }

  @Test
  public void testCreateWithNoName() {
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), nullValue());
  }

  @Test
  public void testCreateWithInvalidName() {
    String groupName = "Testing:" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), nullValue());
  }

  @Test
  public void testCreateWithInvalidLengthName() {
    String groupName = "aadhkasjdhkajshdjashdkjashdkjahskjdhakjshdjashdajshdkajshdk";
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> created = createSkillGroup(skillGroupTo);
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), notNullValue());
    assertThat(created.getErrors().size(), is(1));
    assertThat(created.getErrors().get(0).getCode(), is(ErrorCode.INVALID_FIELD.getCode()));
    assertThat(created.getErrors().get(0).getMessage(), is("groupName: length must be between 0 and 25"));
  }
}
