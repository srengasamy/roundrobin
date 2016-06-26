package com.roundrobin.web;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import com.roundrobin.api.Error;
import com.roundrobin.api.Response;
import com.roundrobin.vault.api.SkillDetailTo;
import com.roundrobin.vault.api.SkillGroupTo;
import com.roundrobin.vault.common.ErrorCode;
import com.roundrobin.vault.domain.Skill;

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
    skillDetailTo.setDeliveryType(Optional.of(Skill.DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = helper
        .post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port)
        .getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    assertThat(createdSkill.getEntity().getName().get(), is(skillName));
    assertThat(createdSkill.getEntity().getId(), notNullValue());
    assertThat(createdSkill.getEntity().getDeliveryType().get(), is(Skill.DeliveryType.HOME));
  }

  @Test
  public void testCreateWithNullValues() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    Response<String> created =
        helper.post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<String>>() {}, port)
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "name: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "skillGroupId: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "deliveryType: may not be null")));
  }

  @Test
  public void testCreateWithInvalidDeliveryType() {
    Map<String, String> skillDetailTo = new HashMap<>();
    skillDetailTo.put("deliveryType", "a");
    Response<String> created =
        helper.post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<String>>() {}, port)
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testCreateWithEmptyValues() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setSkillGroupId(" ");
    skillDetailTo.setName(Optional.of(" "));
    Response<String> created =
        helper.post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<String>>() {}, port)
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "name: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "skillGroupId: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "deliveryType: may not be null")));
  }

  @Test
  public void testCreateWithInvalidValues() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setName(Optional.of(":"));
    Response<String> created =
        helper.post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<String>>() {}, port)
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "name: must match \"^[A-Za-z0-9]*$\""),
            new Error(ErrorCode.INVALID_FIELD, "skillGroupId: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "deliveryType: may not be null")));
  }

  @Test
  public void testCreateWithInvalidNameLength() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo
        .setName(Optional.of("sadasldasldhajshdkasjhdkashdjkashdkjashkjdhasjkdhkjashdkjashdkjashkjdhaskdhakjsdhkjas"));
    Response<String> created =
        helper.post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<String>>() {}, port)
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "name: length must be between 0 and 25"),
            new Error(ErrorCode.INVALID_FIELD, "skillGroupId: may not be empty"),
            new Error(ErrorCode.INVALID_FIELD, "deliveryType: may not be null")));
  }

  @Test
  public void testCreateWithInvalidGroupId() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setName(Optional.of("testing"));
    skillDetailTo.setDeliveryType(Optional.of(Skill.DeliveryType.HOME));
    skillDetailTo.setSkillGroupId("testing");
    Response<String> created =
        helper.post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<String>>() {}, port)
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_GROUP_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_GROUP_ID))));
  }

  @Test
  public void testUpdate() {
    String groupName = "Testing" + System.currentTimeMillis();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> createdGroup = createSkillGroup(skillGroupTo);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(Skill.DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = helper
        .post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port)
        .getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    createdSkill.getEntity().setName(Optional.of("newname"));
    Response<SkillDetailTo> updatedSkill = helper.put(url + "skill-detail", createdSkill.getEntity(),
        new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port).getBody();
    assertThat(updatedSkill.getEntity(), notNullValue());
    Response<SkillDetailTo> read = helper.get(url + "skill-detail/{skillDetailId}",
        new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port, updatedSkill.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getName().get(), is("newname"));
  }

  @Test
  public void testUpdateWithEmptySkillId() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    Response<SkillDetailTo> updatedSkill = helper
        .put(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port)
        .getBody();
    assertThat(updatedSkill.getEntity(), nullValue());
    assertThat(updatedSkill.getErrors(), notNullValue());
    assertThat(updatedSkill.getErrors(), hasItems(new Error(ErrorCode.INVALID_FIELD, "id: may not be empty")));
  }

  @Test
  public void testUpdateWithInvalidSkillId() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setId("testing");
    Response<SkillDetailTo> updatedSkill = helper
        .put(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port)
        .getBody();
    assertThat(updatedSkill.getEntity(), nullValue());
    assertThat(updatedSkill.getErrors(), notNullValue());
    assertThat(updatedSkill.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_DETAIL_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_DETAIL_ID))));
  }

  @Test
  public void testUpdateWithInvalidName() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setName(Optional.of(";."));
    Response<SkillDetailTo> updatedSkill = helper
        .put(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port)
        .getBody();
    assertThat(updatedSkill.getEntity(), nullValue());
    assertThat(updatedSkill.getErrors(), notNullValue());
    assertThat(updatedSkill.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "name: must match \"^[A-Za-z0-9]*$\"")));
  }

  @Test
  public void testUpdateWithInvalidNameLength() {
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setName(Optional.of("asdasdjaskhdkasajsdhajkshdjkashdjkashdjkhasjkdhkajshdkjashdkjash"));
    Response<SkillDetailTo> updatedSkill = helper
        .put(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port)
        .getBody();
    assertThat(updatedSkill.getEntity(), nullValue());
    assertThat(updatedSkill.getErrors(), notNullValue());
    assertThat(updatedSkill.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_FIELD, "name: length must be between 0 and 25")));
  }

  @Test
  public void testUpdateWithInvalidDeliveryType() {
    Map<String, String> skillDetailTo = new HashMap<>();
    skillDetailTo.put("deliveryType", "a");
    Response<String> created =
        helper.put(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<String>>() {}, port)
            .getBody();
    assertThat(created.getEntity(), nullValue());
    assertThat(created.getErrors(),
        hasItems(new Error(ErrorCode.UNPARSABLE_INPUT, messages.getErrorMessage(ErrorCode.UNPARSABLE_INPUT))));
  }

  @Test
  public void testRead() {
    String groupName = "Testing" + System.currentTimeMillis();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> createdGroup = createSkillGroup(skillGroupTo);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(Skill.DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = helper
        .post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port)
        .getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    Response<SkillDetailTo> read = helper.get(url + "skill-detail/{skillDetailId}",
        new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port, createdSkill.getEntity().getId()).getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().getName().get(), is(skillName));
    assertThat(read.getEntity().getDeliveryType().get(), is(Skill.DeliveryType.HOME));
  }

  @Test
  public void testReadEmptyId() {
    Response<List<SkillDetailTo>> read = helper.get(url + "skill-detail/{skillDetailId}",
        new ParameterizedTypeReference<Response<List<SkillDetailTo>>>() {}, port, " ").getBody();
    assertThat(read.getEntity(), notNullValue());
    assertThat(read.getEntity().size(), not(is(0)));
  }

  @Test
  public void testReadEmptyWithInvalidId() {
    Response<SkillDetailTo> read = helper.get(url + "skill-detail/{skillDetailId}",
        new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_DETAIL_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_DETAIL_ID))));
  }

  @Test
  public void testDelete() {
    String groupName = "Testing" + System.currentTimeMillis();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> createdGroup = createSkillGroup(skillGroupTo);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(Skill.DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = helper
        .post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port)
        .getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    Response<Boolean> deleted = helper.delete(url + "skill-detail/{skillDetailId}",
        new ParameterizedTypeReference<Response<Boolean>>() {}, port, createdSkill.getEntity().getId()).getBody();
    assertThat(deleted.getEntity(), notNullValue());
    assertThat(deleted.getEntity(), is(true));
    Response<Boolean> read = helper.get(url + "skill-detail/{skillDetailId}",
        new ParameterizedTypeReference<Response<Boolean>>() {}, port, createdSkill.getEntity().getId()).getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_DETAIL_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_DETAIL_ID))));
  }

  @Test
  public void testDeleteEmptyId() {
    Response<String> read = helper
        .delete(url + "skill-detail/{skillDetailId}", new ParameterizedTypeReference<Response<String>>() {}, port, " ")
        .getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(),
        hasItems(new Error(ErrorCode.INVALID_URL, messages.getErrorMessage(ErrorCode.INVALID_URL))));
  }

  @Test
  public void testDeleteEmptyWithInvalidId() {
    Response<String> read = helper.delete(url + "skill-detail/{skillDetailId}",
        new ParameterizedTypeReference<Response<String>>() {}, port, "testing").getBody();
    assertThat(read.getEntity(), nullValue());
    assertThat(read.getErrors(), hasItems(
        new Error(ErrorCode.INVALID_SKILL_DETAIL_ID, messages.getErrorMessage(ErrorCode.INVALID_SKILL_DETAIL_ID))));
  }

  @Test
  public void testList() {
    Response<List<SkillDetailTo>> list = helper
        .get(url + "skill-detail", new ParameterizedTypeReference<Response<List<SkillDetailTo>>>() {}, port).getBody();
    assertThat(list.getEntity(), notNullValue());
    int size = list.getEntity().size();
    String groupName = "Testing" + System.currentTimeMillis();
    String skillName = "Testing" + System.currentTimeMillis();
    SkillGroupTo skillGroupTo = new SkillGroupTo();
    skillGroupTo.setGroupName(Optional.of(groupName));
    Response<SkillGroupTo> createdGroup = createSkillGroup(skillGroupTo);
    SkillDetailTo skillDetailTo = new SkillDetailTo();
    skillDetailTo.setDeliveryType(Optional.of(Skill.DeliveryType.HOME));
    skillDetailTo.setName(Optional.of(skillName));
    skillDetailTo.setSkillGroupId(createdGroup.getEntity().getId());
    Response<SkillDetailTo> createdSkill = helper
        .post(url + "skill-detail", skillDetailTo, new ParameterizedTypeReference<Response<SkillDetailTo>>() {}, port)
        .getBody();
    assertThat(createdSkill.getEntity(), notNullValue());
    list = helper.get(url + "skill-detail", new ParameterizedTypeReference<Response<List<SkillDetailTo>>>() {}, port)
        .getBody();
    assertThat(list.getEntity(), notNullValue());
    assertThat(list.getEntity().size(), is(size + 1));
  }
}
